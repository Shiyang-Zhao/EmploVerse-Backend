package com.java.springboot.EMSbackend.service.userService;

import com.java.springboot.EMSbackend.dto.UserDto;
import com.java.springboot.EMSbackend.model.jwtModel.JwtRequest;
import com.java.springboot.EMSbackend.model.userModel.User;

import jakarta.servlet.http.HttpServletRequest;

public interface JwtService {

    User registeUser(UserDto userDto) throws Exception;

    String authenticateUser(JwtRequest authenticationRequest) throws Exception;

    String logoutUser(HttpServletRequest request);
}
