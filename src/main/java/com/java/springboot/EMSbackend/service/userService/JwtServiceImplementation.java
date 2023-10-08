package com.java.springboot.EMSbackend.service.userService;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
import com.java.springboot.EMSbackend.service.S3Service.S3Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JwtServiceImplementation implements JwtService {

    @Value("${cookie.maxAge}")
    private long cookieMaxAge;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtTokenUtil jwtTokenUtil;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final UserRepository userRepository;

    private final S3Service s3Service;

    @Autowired
    public JwtServiceImplementation(
            BCryptPasswordEncoder passwordEncoder,
            JwtTokenUtil jwtTokenUtil,
            AuthenticationManager authenticationManager,
            UserService userService,
            UserRepository userRepository,
            EmployeeRepository employeeRepository, S3Service s3Service) {
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userRepository = userRepository;
        this.s3Service = s3Service;
    }

    @Override
    public User registerUser(UserDto userDto) throws Exception {
        if (userDto.getPassword1() == null || userDto.getPassword2() == null
                || !userDto.getPassword1().equals(userDto.getPassword2())) {
            throw new IllegalArgumentException("Passwords do not match or are null");
        }
        String profileImagePath = s3Service.uploadProfileImageToS3(userDto);
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
            final String jwt = jwtTokenUtil.generateToken(userDetails, authMethod, authorities);

            String cookieValue = String.format(
                    "jwt=%s; authMethod=%s; Max-Age=%s; Path=/; Secure; HttpOnly; SameSite=None",
                    jwt, authMethod, cookieMaxAge);
            response.setHeader("Set-Cookie", cookieValue);
            return jwt;
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @Override
    public Map<String, Object> checkAuth(HttpServletRequest request) {
        String jwt = jwtTokenUtil.getJwtFromRequest(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (jwt != null) {
            UserDetails userDetails = userService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(jwt));
            if (userDetails != null && authentication != null && jwtTokenUtil.validateToken(jwt, userDetails)
                    && authentication.isAuthenticated()) {
                Map<String, Object> claims = new HashMap<>(jwtTokenUtil.getAllClaimsFromToken(jwt));
                return claims;
            }
        }
        return null;
    }

    @Override
    public String logoutUser(HttpServletRequest request, HttpServletResponse response) throws Exception {

        try {
            String jwt = jwtTokenUtil.getJwtFromRequest(request);
            UserDetails userDetails = userService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(jwt));
            if (jwt != null && jwtTokenUtil.validateToken(jwt, userDetails)) {
                jwtTokenUtil.blacklistToken(jwt);
                response.setHeader("Set-Cookie",
                        "jwt=; authMethod=; Max-Age=0; Path=/; Secure; HttpOnly; SameSite=None");
                SecurityContextHolder.clearContext();
            }
            return "Logged out successfully";
        } catch (Exception e) {
            throw new Exception("Failed to log out the user: " + e.getMessage());
        }
    }

}
