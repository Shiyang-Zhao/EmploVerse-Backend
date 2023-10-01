package com.java.springboot.EMSbackend.service.S3Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Path;

@Service
public class S3Service {

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public void uploadFile(String keyName, Path filePath) {
        s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(keyName).build(),
                RequestBody.fromFile(filePath));
    }

}
