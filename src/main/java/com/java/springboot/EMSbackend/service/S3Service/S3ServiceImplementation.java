package com.java.springboot.EMSbackend.service.S3Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.java.springboot.EMSbackend.dto.UserDto.UserDto;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Service
public class S3ServiceImplementation implements S3Service {

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.baseUrl}")
    private String baseUrl;

    @Value("${aws.s3.profileImageFolder}")
    private String profileImageFolder;

    @Value("${aws.s3.defaultImagePath}")
    private String defaultImagePath;

    private void uploadFile(String keyName, InputStream data) throws Exception {
        s3Client.putObject(
                PutObjectRequest.builder().bucket(bucketName).key(keyName).build(),
                RequestBody.fromInputStream(data, data.available()));
    }

    private String generateFilename(UserDto userDto, MultipartFile file) {
        try {
            // Hashing the file content
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = file.getBytes();
            digest.update(bytes);
            byte[] hashBytes = digest.digest();
            String fileHash = Base64.getEncoder().encodeToString(hashBytes).replaceAll("[/+=]", "_");

            // Generating a UUID
            String uniqueID = UUID.randomUUID().toString();

            // Getting file extension
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

            // Creating a filename with username, hash, and UUID
            String filename = userDto.getUsername() + "-" + fileHash + "-" + uniqueID + "." + fileExtension;

            return filename;
        } catch (IOException | NoSuchAlgorithmException e) {
            // Handle exceptions (maybe log an error message or throw a custom exception)
            throw new RuntimeException("Error generating filename", e);
        }
    }

    @Override
    public String getPreUploadS3Path(UserDto userDto) {
        MultipartFile profileImage = userDto.getProfileImage();
        if (profileImage == null || profileImage.isEmpty()) {
            return defaultImagePath;
        }
        String filename = generateFilename(userDto, profileImage);
        return profileImageFolder + "/" + userDto.getUsername() + "/" + filename;
    }

    @Override
    public String uploadProfileImageToS3(UserDto userDto) {
        try {
            String s3Path = getPreUploadS3Path(userDto);

            try (InputStream inputStream = userDto.getProfileImage().getInputStream()) {
                uploadFile(s3Path, inputStream);
            }
            return baseUrl + "/" + s3Path;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload profile image to S3", e);
        }
    }

}
