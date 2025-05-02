package com.example.XDMHPL_Back_end.Configuration;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // 🔹 Thêm "/queue" để hỗ trợ tin nhắn riêng tư
        registry.setApplicationDestinationPrefixes("/app"); // 🔹 Prefix để client gửi tin nhắn đến server
        registry.setUserDestinationPrefix("/user");// Client gửi tin nhắn đến server
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:5173") // Cho phép tất cả frontend kết nối
                .withSockJS();

        // 🔔 Endpoint riêng cho notification
        registry.addEndpoint("/ws-notification")
                .setAllowedOriginPatterns("http://localhost:5173")
                .withSockJS();
    }

    @Override

    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    List<String> usernameHeaders = accessor.getNativeHeader("username");
                    if (usernameHeaders != null && !usernameHeaders.isEmpty()) {
                        String username = usernameHeaders.get(0);
                        System.out.println("Setting principal for user: " + username);

                        // Sử dụng Principal đơn giản hơn
                        accessor.setUser(new Principal() {
                            @Override
                            public String getName() {
                                return username;
                            }
                        });
                    }
                }
                return message;
            }
        });
    }
}
