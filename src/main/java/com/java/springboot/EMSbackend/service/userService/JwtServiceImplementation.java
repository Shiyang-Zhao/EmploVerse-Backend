package com.java.springboot.EMSbackend.service.userService;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Collection;

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
    public User registerUser(UserDto userDto) throws Exception {
        if (userDto.getPassword1() == null || userDto.getPassword2() == null
                || !userDto.getPassword1().equals(userDto.getPassword2())) {
            System.out.println(userDto.getPassword1());
            System.out.println(userDto.getPassword2());

            throw new IllegalArgumentException("Passwords do not match or are null");
        }

        String profileImagePath = userService.uploadProfileImageToS3(userDto);

        User newUser = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getUsername(),
                userDto.getEmail(), passwordEncoder.encode(userDto.getPassword1()),
                userDto.getPhoneNumber(), profileImagePath, userDto.getRoles());
        userRepository.save(newUser);

        return newUser;
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

            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true); // Ensure cookie is sent only over HTTPS
            cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days expiration
            cookie.setPath("/");
            response.addCookie(cookie);

            return token;
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @Override
    public String checkAuth(HttpServletRequest request) throws Exception {
        try {
            String jwt = jwtTokenUtil.getJwtFromRequest(request);
            if (jwt == null || jwt.isEmpty()) {
                throw new Exception("No JWT found in the request.");
            }
            String username = jwtTokenUtil.getUsernameFromToken(jwt);
            if (username == null || username.isEmpty()) {
                throw new Exception("Invalid JWT token.");
            }

            UserDetails userDetails = userService.loadUserByUsername(username);

            if (!jwtTokenUtil.validateToken(jwt, userDetails)) {
                throw new Exception("Token validation failed.");
            }
            return "User " + username + " is authenticated.";
        } catch (Exception e) {
            throw new Exception("Failed to check authentication: " + e.getMessage());
        }
    }

    @Override
    public String logoutUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SecurityContextHolder.clearContext();
        try {
            String jwt = jwtTokenUtil.getJwtFromRequest(request);

            // If the JWT token exists, check its status
            if (jwt != null) {
                // If the token is already blacklisted or expired, handle them together
                if (jwtTokenUtil.isTokenBlacklisted(jwt) || jwtTokenUtil.isTokenExpired(jwt)) {
                    // Here, you can decide whether you want to give a generic message
                    // or determine which specific condition was met and give a more tailored
                    // message
                    return "You have already been logged out.";
                }
                // If the token is neither blacklisted nor expired, blacklist it now
                jwtTokenUtil.blacklistToken(jwt);
            }

            // Create and set the jwt cookie to expire immediately
            Cookie cookie = new Cookie("jwt", null);
            cookie.setMaxAge(0);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "Logged out successfully";
        } catch (Exception e) {
            throw new Exception("Failed to log out the user: " + e.getMessage());
        }
    }

}
