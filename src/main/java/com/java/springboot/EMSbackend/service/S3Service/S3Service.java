package com.java.springboot.EMSbackend.service.S3Service;

import com.java.springboot.EMSbackend.dto.UserDto.UserDto;

public interface S3Service {

    String getPreUploadS3Path(UserDto userDto);

    String getFullS3Path(String s3Path);

    String uploadProfileImageToS3(UserDto userDto, String s3Path) throws Exception;

}
