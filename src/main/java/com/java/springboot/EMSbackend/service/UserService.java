package com.java.springboot.EMSbackend.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.java.springboot.EMSbackend.dto.UserDto;
import com.java.springboot.EMSbackend.model.User;

public interface UserService {

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    List<User> getAllUsers();

    User getUserById(long id);

    User getUserByEmail(String email);

    void updateUserById(long id, UserDto userDto);

    void updateUserByEmail(String email, UserDto userDto);

    void deleteUserById(long id);

    void deleteUserByEmail(String email);

    Page<User> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}
