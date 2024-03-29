package com.java.springboot.EMSbackend.service.userService;

import java.util.Map;

import com.java.springboot.EMSbackend.dto.UserDto.UserDto;
import com.java.springboot.EMSbackend.model.userModel.JwtRequest;
import com.java.springboot.EMSbackend.model.userModel.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface JwtService {

    User registerUser(UserDto userDto) throws Exception;

    String authenticateUser(JwtRequest request, HttpServletResponse response) throws Exception;

    Map<String, Object> checkAuth(HttpServletRequest request);

    String logoutUser(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
