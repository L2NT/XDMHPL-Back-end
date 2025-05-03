package com.example.XDMHPL_Back_end.Configuration;

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
        // Đăng ký endpoint cho WebSocket, cho phép kết nối từ địa chỉ frontend
        registry.addEndpoint("/chat")
                .setAllowedOrigins("http://localhost:5173") // Cho phép kết nối từ frontend tại localhost:3000
                .withSockJS(); // Sử dụng SockJS để hỗ trợ các trình duyệt không hỗ trợ WebSocket
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Cấu hình message broker cho WebSocket
        config.enableSimpleBroker("/topic", "/queue"); // Hỗ trợ các topic và queue
        config.setApplicationDestinationPrefixes("/app"); // Chỉ định prefix cho các endpoint của client
        config.setUserDestinationPrefix("/user"); // Để gửi tin nhắn riêng biệt cho mỗi user
    }
}

