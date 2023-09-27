package com.java.springboot.EMSbackend.service.userService;

import java.io.File;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.java.springboot.EMSbackend.dto.UserDto.UserDto;
import com.java.springboot.EMSbackend.model.userModel.JwtRequest;
import com.java.springboot.EMSbackend.model.userModel.JwtTokenUtil;
import com.java.springboot.EMSbackend.model.userModel.User;
import com.java.springboot.EMSbackend.repository.EmployeeRepository;
import com.java.springboot.EMSbackend.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JwtServiceImplementation implements JwtService {

    // Define the base directory to store profile images

    @Value("${empverse.base-image-dir}")
    private String baseProfileImageDir;

    @Value("${empverse.default-image-path}")
    private String defaultProfileImagePath;

    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public JwtServiceImplementation(
            BCryptPasswordEncoder passwordEncoder,
            JwtTokenUtil jwtTokenUtil,
            AuthenticationManager authenticationManager,
            UserService userService,
            UserRepository userRepository,
            EmployeeRepository employeeRepository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public User registeUser(UserDto userDto) throws Exception {
        try {
            String profileImagePath = defaultProfileImagePath;
            // Create a subdirectory for the user based on their ID or username
            String userSubdirectory = baseProfileImageDir + "/" + userDto.getUsername();
            File directory = new File(userSubdirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Handle profile image upload and storage
            MultipartFile profileImage = userDto.getProfileImage();

            if (profileImage != null && !profileImage.isEmpty()) {
                // Generate a unique filename for the profile image
                String originalFilename = profileImage.getOriginalFilename();
                System.out.println("Orignal File Name: " + originalFilename);

                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                String filename = UUID.randomUUID().toString() + "." + fileExtension;
                profileImagePath = userSubdirectory + "/" + filename;
                System.out.println("File Extension: " + originalFilename);

                System.out.println("File Name: " + filename);
                // Save the profile image to the user's subdirectory
                Path destinationFile = Paths.get(userSubdirectory, filename);
                Files.copy(profileImage.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            }

            if (userDto.getPassword1().equals(userDto.getPassword2())) {
                User newUser = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getUsername(),
                        userDto.getEmail(),
                        passwordEncoder.encode(userDto.getPassword1()), userDto.getPhoneNumber(), profileImagePath,
                        userDto.getRoles());
                userRepository.save(newUser);
                return newUser;
            } else {
                throw new IllegalArgumentException("Passwords do not match");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create a new user: " + e.getMessage());
        }
    }

    @Override
    public String authenticateUser(JwtRequest request, HttpServletResponse response)
            throws Exception {
        try {
            UserDetails userDetails = userService.loadUserByUsername(request.getUsernameOrEmail());
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            for (String role : request.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
            // Check if all required roles are present in the user's authorities.
            if (!userDetails.getAuthorities().containsAll(authorities)) {
                throw new AccessDeniedException("INVALID_ROLE");
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsernameOrEmail(),
                    request.getPassword(),
                    authorities // Pass the authorities directly here
            ));
            String authMethod = request.getUsernameOrEmail().contains("@") ? "email" : "username";
            final String token = jwtTokenUtil.generateToken(userDetails, authMethod, authorities);

            Cookie cookie = new Cookie("JWT-TOKEN", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true); // send the cookie over HTTPS only
            cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days expiration
            cookie.setPath("/");
            response.addCookie(cookie); // Add the cookie to the response
            return token;
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @Override
    public String logoutUser(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        // Retrieve JWT-TOKEN cookie from request
        String jwtToken = jwtTokenUtil.getJwtFromRequest(request);
        // If the JWT token exists and is not expired, blacklist it
        if (jwtToken != null) {
            try {
                if (jwtTokenUtil.isTokenExpired(jwtToken)) {
                    return "Token is already expired. You have been logged out.";
                }
                jwtTokenUtil.blacklistToken(jwtToken);
            } catch (Exception e) {
                return "Error validating the token. Please try again or reauthenticate.";
            }
        }
        // Create and set the JWT-TOKEN cookie to expire immediately
        Cookie cookie = new Cookie("JWT-TOKEN", null);
        cookie.setMaxAge(0); // immediately expire the cookie
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "Logged out successfully";
    }

}
