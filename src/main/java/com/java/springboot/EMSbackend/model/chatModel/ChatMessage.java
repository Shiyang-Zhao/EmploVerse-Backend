package com.java.springboot.EMSbackend.model.chatModel;

import java.time.LocalDateTime;

import com.java.springboot.EMSbackend.model.userModel.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User sender; // Assuming a User entity exists

    private String content;

    private LocalDateTime timestamp;

    // Getters and setters
}
