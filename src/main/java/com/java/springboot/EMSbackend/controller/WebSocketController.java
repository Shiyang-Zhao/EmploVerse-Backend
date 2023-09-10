package com.java.springboot.EMSbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/hello")
    public void greeting(@Payload String message, @Header("Authorization") String jwt)
            throws Exception {

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
            log.info("Received hello: {} with {}", message, jwt);
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