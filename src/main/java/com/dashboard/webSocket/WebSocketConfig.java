package com.dashboard.webSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트 연결 URL: /ws-dashboard
        registry.addEndpoint("/ws-dashboard")
                .setAllowedOriginPatterns("*") // 모든 도메인 허용
                .withSockJS(); // SockJS fallback 허용
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 구독용 경로 prefix: /topic
        registry.enableSimpleBroker("/topic");
        // 클라이언트에서 메시지 전송할 때 prefix: /app
        registry.setApplicationDestinationPrefixes("/app");
    }

}
