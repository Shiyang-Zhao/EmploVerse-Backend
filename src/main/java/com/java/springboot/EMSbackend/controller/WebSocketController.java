package com.java.springboot.EMSbackend.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class WebSocketController {

    @MessageMapping("/hello")
    @SendTo("/toClient/greetings")
    public String greeting(String message) throws Exception {
        log.info("Received hello: {}", message);
        return "Hello, " + message + "!";
    }
}