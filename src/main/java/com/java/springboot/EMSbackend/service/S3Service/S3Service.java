package com.java.springboot.EMSbackend.service.S3Service;

import java.io.InputStream;

public interface S3Service {
    void uploadFile(String keyName, InputStream data) throws Exception;
}
