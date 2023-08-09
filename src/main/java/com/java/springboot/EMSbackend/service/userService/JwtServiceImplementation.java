package com.java.springboot.EMSbackend.service.userService;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import javax.imageio.ImageIO;

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
import org.springframework.web.multipart.MultipartFile;

import com.java.springboot.EMSbackend.config.JwtTokenUtil;
import com.java.springboot.EMSbackend.dto.UserDto.UserDto;
import com.java.springboot.EMSbackend.model.userModel.JwtRequest;
import com.java.springboot.EMSbackend.model.userModel.User;
import com.java.springboot.EMSbackend.repository.EmployeeRepository;
import com.java.springboot.EMSbackend.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtServiceImplementation implements JwtService {

    // Define the base directory to store profile images
    private static final String BASE_PROFILE_IMAGE_DIR = "C:/Users/shiya/Downloads/Projects/EmploVerse/EmploVerse-Frontend/src/media/profileImages";
    private static final String DEFAULT_PROFILE_IMAGE_PATH = "C:/Users/shiya/Downloads/Projects/EmploVerse/EmploVerse-Frontend/src/media/profileImages/defaultProfileImage.jpg";

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
            String profileImagePath = DEFAULT_PROFILE_IMAGE_PATH;
            // Create a subdirectory for the user based on their ID or username
            String userSubdirectory = BASE_PROFILE_IMAGE_DIR + "/" + userDto.getUsername();
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

                // Convert the MultipartFile to a BufferedImage
                // BufferedImage bufferedImage = ImageIO.read(profileImage.getInputStream());

                System.out.println("File Name: " + filename);
                // Save the profile image to the user's subdirectory
                Path destinationFile = Paths.get(userSubdirectory, filename);
                Files.copy(profileImage.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

                // Save the BufferedImage as an image file
                // File imageFile = new File(profileImagePath);
                // ImageIO.write(bufferedImage, fileExtension, imageFile);
            }

            User newUser = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getUsername(),
                    userDto.getEmail(),
                    passwordEncoder.encode(userDto.getPassword()), userDto.getPhoneNumber(), profileImagePath,
                    userDto.getRoles());
            userRepository.save(newUser);
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

            // Add logging to see the roles in the JwtRequest and the roles present in
            // UserDetails
            System.out.println("JwtRequest roles: " + authenticationRequest.getRoles());
            System.out.println("UserDetails authorities: " + userDetails.getAuthorities());
            System.out.println("UserDetails authorities: " + userDetails.getPassword());

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
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                // Check if the token is expired
                if (jwtTokenUtil.isTokenExpired(jwtToken)) {
                    return "Token is already expired. No need to logout.";
                }
            } catch (Exception e) {
                // Handle any potential exceptions related to token parsing or validation
                // Optionally log the error
                return "Error validating the token. Please try again or reauthenticate.";
            }
        }
        jwtTokenUtil.blacklistToken(jwtToken);
        return "Logged out successfully";
    }
}
