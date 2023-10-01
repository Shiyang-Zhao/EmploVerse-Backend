package com.java.springboot.EMSbackend.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.java.springboot.EMSbackend.model.chatModel.ChatMessage;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    private Map<String, String> userRoomMap = new ConcurrentHashMap<>();

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat/{room}") // Define a message mapping with a dynamic room variable
    public void handleMessage(@DestinationVariable String room, ChatMessage message) {
        String destination = "/topic/" + room;

        messagingTemplate.convertAndSend(destination, message.getContent());
    }

    @SubscribeMapping("/chat/join/{room}")
    public void handleJoin(@DestinationVariable String room) {
        String destination = "/topic/" + room;

        // Join the room
        userRoomMap.put(SecurityContextHolder.getContext().getAuthentication().getName(), room);

        // Send a welcome message or perform any other necessary actions
        messagingTemplate.convertAndSend(destination, "Welcome to room " + room);
    }

    @MessageMapping("/hello")
    public void greeting(@Payload String message, @Header("Authorization") String jwt)
            throws Exception {

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
            log.info("Received message: {} with jwt: {}", message, jwt);
            Message<String> msg = MessageBuilder.withPayload(message)
                    .setHeader("Authorization", "Bearer " + jwt) // Add JWT as a header
                    .build();
            messagingTemplate.convertAndSend("/toClient/greetings", msg);
        } else {
            // Handle case where JWT is not present or in an unexpected format.
            throw new RuntimeException("Invalid or missing JWT token");
        }
    }

}