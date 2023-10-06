package com.java.springboot.EMSbackend.service.S3Service;

import com.java.springboot.EMSbackend.dto.UserDto.UserDto;

public interface S3Service {

    String uploadProfileImageToS3(UserDto userDto) throws Exception;

}
