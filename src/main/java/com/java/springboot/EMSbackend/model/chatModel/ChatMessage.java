package com.java.springboot.EMSbackend.model.chatModel;

import com.java.springboot.EMSbackend.model.userModel.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Entity
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User sender;

    @NotNull
    @Size(max = 500)
    private String content;

    @NotNull
    private LocalDateTime timestamp;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private ChatRoom chatRoom;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    private LocalDateTime lastEditedTimestamp;
}

enum MessageStatus {
    SENT,
    DELIVERED,
    READ
}
