package com.java.springboot.EMSbackend.config.WebSocket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketSecurityConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketChannelInterceptor webSocketChannelInterceptor;

    public WebSocketSecurityConfig(WebSocketChannelInterceptor webSocketChannelInterceptor) {
        this.webSocketChannelInterceptor = webSocketChannelInterceptor;
    }

    @Value("${empverse.cors.origins}")
    private String allowedOrigins;

    @Bean
    public CorsFilter corsFilter() {

        CorsConfiguration config = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Splitting origins into an array and add each origin
        for (String allowedOrigin : allowedOrigins.split(",")) {
            config.addAllowedOriginPattern(allowedOrigin.trim()); // Consider using allowed origin patterns
        }
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    // Handle the Cors
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // registry.addEndpoint("/ws")
        // .setAllowedOrigins(allowedOrigins);
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns(allowedOrigins.split(","))
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/toClient", "/queue");
        config.setApplicationDestinationPrefixes("/toServer");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketChannelInterceptor);
    }

}
