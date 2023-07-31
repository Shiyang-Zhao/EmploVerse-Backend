package com.java.springboot.EMSbackend.service.userService;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.java.springboot.EMSbackend.config.JwtTokenUtil;
import com.java.springboot.EMSbackend.dto.UserDto;
import com.java.springboot.EMSbackend.model.employeeModel.Employee;
import com.java.springboot.EMSbackend.model.jwtModel.JwtRequest;
import com.java.springboot.EMSbackend.model.userModel.User;
import com.java.springboot.EMSbackend.repository.EmployeeRepository;
import com.java.springboot.EMSbackend.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtServiceImplementation implements JwtService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

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
        this.employeeRepository = employeeRepository;
    }

    @Override
    public User registeUser(UserDto userDto) throws Exception {
        try {
            User newUser = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getUsername(),
                    userDto.getEmail(),
                    passwordEncoder.encode(userDto.getPassword()), userDto.getPhoneNumber(), userDto.getProfileImage(),
                    userDto.getRoles());

            userRepository.save(newUser);
            boolean isAdmin = userDto.getRoles().stream()
                    .anyMatch(role -> role.getName().equalsIgnoreCase("ROLE_ADMIN"));
            if (!isAdmin) {
                employeeRepository.save(new Employee(newUser));
            }
            return newUser;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create a new user: " + e.getMessage());
        }
    }

    @Override
    public String authenticateUser(JwtRequest authenticationRequest) throws Exception {
        try {
            UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
            // Assuming the roles in JwtRequest are simple strings (not GrantedAuthority
            // objects).
            // Check if all required roles are present in the user's authorities.
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            for (String role : authenticationRequest.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(role));
            }

            // Check if all required roles are present in the user's authorities.
            if (!userDetails.getAuthorities().containsAll(authorities)) {
                throw new AccessDeniedException("INVALID_ROLE");
            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword(),
                    authorities // Pass the authorities directly here
            ));

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
