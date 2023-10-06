package com.java.springboot.EMSbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.springboot.EMSbackend.dto.UserDto.UserDto;
import com.java.springboot.EMSbackend.model.userModel.JwtRequest;
import com.java.springboot.EMSbackend.model.userModel.JwtResponse;
import com.java.springboot.EMSbackend.service.userService.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    @Autowired
    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@ModelAttribute UserDto userDto) throws Exception {
        return ResponseEntity.ok(jwtService.registerUser(userDto));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody JwtRequest request,
            HttpServletResponse response)
            throws Exception {
        String token = jwtService.authenticateUser(request, response);
        return ResponseEntity
                .ok(new JwtResponse(token, request.getRoles()));
    }

    // Only authenticated accounts can log out
    @PostMapping("/logout")
    // @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String message = jwtService.logoutUser(request, response);
        return ResponseEntity.ok(message);
    }
}
