package com.java.springboot.EMSbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.java.springboot.EMSbackend.model.userModel.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    void deleteById(long id);

    void deleteByUsername(String username);

    void deleteByEmail(String email);
}
