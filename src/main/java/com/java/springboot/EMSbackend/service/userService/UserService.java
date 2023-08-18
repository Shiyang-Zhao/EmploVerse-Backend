package com.java.springboot.EMSbackend.service.userService;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import com.java.springboot.EMSbackend.dto.UserDto.UserDto;
import com.java.springboot.EMSbackend.model.userModel.User;
import com.java.springboot.EMSbackend.model.employeeModel.Employee;

public interface UserService {

    static final String BASE_PROFILE_IMAGE_DIR = "C:/Users/shiya/Downloads/Projects/EmploVerse/EmploVerse-Frontend/src/media/profileImages";

    static final String DEFAULT_PROFILE_IMAGE_PATH = "C:/Users/shiya/Downloads/Projects/EmploVerse/EmploVerse-Frontend/src/media/profileImages/defaultProfileImage.jpg";

    UserDetails loadUserByUsername(String username);

    List<User> getAllUsers();

    User getUserById(long id);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    User getUserByUsernameOrEmail(String usernameOrEmail);

    void updateUserById(long id, UserDto userDto);

    void updateUserByUsername(String username, UserDto userDto);

    void updateUserByEmail(String email, UserDto userDto);

    void deleteUserById(long id);

    void deleteUserByUsername(String username);

    void deleteUserByEmail(String email);

    Page<User> getPaginatedUsers(List<User> userList, int pageNo, int pageSize, String sortField,
            String sortDir);

    List<User> searchUsers(String keyword, String searchField);

    // List<User> sortUsers(List<User> users, String sortField, String sortDir);

    User getCurrentUser();

    void updateCurrentUser(UserDto userDto);

    void addCurrentUserToEmployees();

    void updateCurrentUserPassword(String newPassword);

    void updateCurrentUserProfileIamge(MultipartFile newProfileImage);

    Employee getCurrentEmployee();
}
