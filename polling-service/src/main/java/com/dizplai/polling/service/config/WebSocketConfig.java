package com.dizplai.polling.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

//@Configuration
//@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple memory-based message broker
        config.enableSimpleBroker("/topic"); // Prefix for outgoing messages
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register an endpoint that the clients will use to connect
        registry.addEndpoint("/polling-websocket") // The WebSocket endpoint
                .setAllowedOrigins("*") // Allow cross-origin requests
                .withSockJS(); // Fallback for browsers that don't support WebSocket
    }
}
