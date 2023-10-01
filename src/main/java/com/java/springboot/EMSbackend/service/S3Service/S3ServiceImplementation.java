package com.java.springboot.EMSbackend.service.S3Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

@Service
public class S3ServiceImplementation implements S3Service {

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Override
    public void uploadFile(String keyName, InputStream data) throws Exception {
        s3Client.putObject(
                PutObjectRequest.builder().bucket(bucketName).key(keyName).build(),
                RequestBody.fromInputStream(data, data.available()));
    }

}