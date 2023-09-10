package com.java.springboot.EMSbackend.config.WebSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import com.java.springboot.EMSbackend.model.userModel.JwtTokenUtil;

@Component
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenUtil jwtTokenUtil;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    public WebSocketChannelInterceptor(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // Check if this is a CONNECT message (WebSocket handshake)
        if (StompCommand.CONNECT == accessor.getCommand() || StompCommand.SUBSCRIBE == accessor.getCommand() || StompCommand.SEND == accessor.getCommand()) {
            // Extract the JWT token from the headers
            String jwt = accessor.getFirstNativeHeader("Authorization").substring(7);

            if (jwt != null && !jwt.isEmpty() && !jwtTokenUtil.isTokenExpired(jwt)
                    && !jwtTokenUtil.isTokenBlacklisted(jwt)) {
                logger.info("JWT IS GOod");
                return message;
            }
        }
        logger.info(accessor.getCommand().toString());
        logger.info("Error");
        return null;
    }

}
