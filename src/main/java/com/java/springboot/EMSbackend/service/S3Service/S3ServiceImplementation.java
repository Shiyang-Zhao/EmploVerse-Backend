package com.java.springboot.EMSbackend.service.S3Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.java.springboot.EMSbackend.dto.UserDto.UserDto;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.util.UUID;

@Service
public class S3ServiceImplementation implements S3Service {

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${empverse.base-image-path}")
    private String baseProfileImagePath;

    @Value("${empverse.default-image-path}")
    private String defaultProfileImagePath;

    private void uploadFile(String keyName, InputStream data) throws Exception {
        s3Client.putObject(
                PutObjectRequest.builder().bucket(bucketName).key(keyName).build(),
                RequestBody.fromInputStream(data, data.available()));
    }

    @Override
    public String uploadProfileImageToS3(UserDto userDto) {
        try {
            MultipartFile profileImage = userDto.getProfileImage();

            if (profileImage == null || profileImage.isEmpty()) {
                return defaultProfileImagePath;
            }

            String originalFilename = profileImage.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String filename = UUID.randomUUID().toString() + "." + fileExtension;
            String s3Path = userDto.getUsername() + "/" + filename;

            try (InputStream inputStream = profileImage.getInputStream()) {
                uploadFile(s3Path, inputStream);
            }
            return baseProfileImagePath + s3Path;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload profile image to S3", e);
        }
    }

}
