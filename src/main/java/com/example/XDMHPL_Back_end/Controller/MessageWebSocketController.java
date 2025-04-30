package com.example.XDMHPL_Back_end.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.XDMHPL_Back_end.DTO.Message;
import com.example.XDMHPL_Back_end.DTO.MessageRequest;
import com.example.XDMHPL_Back_end.Services.MessageService;

@Controller
public class MessageWebSocketController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // Gửi tin nhắn qua WebSocket
    @MessageMapping("/chat/send")
    public void sendMessage(@Payload MessageRequest request) {
        try {
            // Gửi tin nhắn vào DB và trả về message
            Message savedMessage = messageService.sendMessage(
                request.getSenderId(),
                request.getReceiverId(),
                request.getText(),
                request.getChatBoxId(),
                request.getMediaList()
            );

            // Gửi tin nhắn qua WebSocket tới các client
            simpMessagingTemplate.convertAndSend("/topic/messages", savedMessage);

        } catch (Exception ex) {
            // Xử lý lỗi nếu có (tùy vào yêu cầu của bạn)
            simpMessagingTemplate.convertAndSendToUser(
                request.getSenderId().toString(),
                "/queue/errors",
                ex.getMessage()
            );
        }
    }
}


