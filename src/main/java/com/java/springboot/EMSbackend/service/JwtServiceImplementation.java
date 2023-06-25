package com.java.springboot.EMSbackend.service;

import java.nio.file.AccessDeniedException;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.java.springboot.EMSbackend.config.JwtTokenUtil;
import com.java.springboot.EMSbackend.dto.UserDto;
import com.java.springboot.EMSbackend.model.JwtRequest;
import com.java.springboot.EMSbackend.model.User;
import com.java.springboot.EMSbackend.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtServiceImplementation implements JwtService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    private UserRepository userRepository;

    @Autowired
    public JwtServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registeUser(UserDto userDto) throws Exception {
        try {
            User newUser = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(),
                    passwordEncoder.encode(userDto.getPassword()), userDto.getBirthday(),
                    userDto.getPhoneNumber(), userDto.getAddress1(), userDto.getAddress2(), userDto.getCity(),
                    userDto.getState(),
                    userDto.getZipCode(), userDto.getCountry(), userDto.getCompany(), userDto.getStartDate(),
                    userDto.getEndDate(), userDto.getDepartment(), userDto.getSupervisor(),
                    userDto.getJobTitles(), userDto.getWorkSchedule(), userDto.getStatus(), userDto.getUniversity(),
                    userDto.getDegree(),
                    userDto.getMajor(), userDto.getRoles());
            return userRepository.save(newUser);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create a new user: " + e.getMessage());
        }
    }

    @Override
    public String authenticateUser(JwtRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()));
            UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getEmail());
            Collection<String> authorities = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            if (!authorities.containsAll(authenticationRequest.getRoles())) {
                throw new AccessDeniedException("INVALID_ROLE");
            }
            final String token = jwtTokenUtil.generateToken(userDetails);
            return token;
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @Override
    public String logoutUser(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        final String requestTokenHeader = request.getHeader("Authorization");

        String jwtToken = null;
        jwtToken = requestTokenHeader.substring(7);
        jwtTokenUtil.blacklistToken(jwtToken);

        return "Logged out successfully";
    }
}
