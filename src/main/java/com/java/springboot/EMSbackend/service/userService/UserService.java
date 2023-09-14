package com.java.springboot.EMSbackend.service.userService;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import com.java.springboot.EMSbackend.dto.UserDto.UserDto;
import com.java.springboot.EMSbackend.model.userModel.User;
import com.java.springboot.EMSbackend.model.employeeModel.Employee;

public interface UserService {

    UserDetails loadUserByUsername(String username);

    List<User> getAllUsers();

    User getUserById(long id);

    // User getUserByUsername(String username);

    // User getUserByEmail(String email);

    User getUserByUsernameOrEmail(String usernameOrEmail);

    void updateUserById(long id, UserDto userDto);

    // void updateUserByUsername(String username, UserDto userDto);

    // void updateUserByEmail(String email, UserDto userDto);

    void deleteUserById(long id);

    void deleteUserByUsername(String username);

    void deleteUserByEmail(String email);

    Page<User> getPaginatedUsers(int pageNo, int pageSize, String sortField,
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
