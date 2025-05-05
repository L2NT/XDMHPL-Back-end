package com.example.XDMHPL_Back_end.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.XDMHPL_Back_end.DTO.MessageMediaDTO;
import com.example.XDMHPL_Back_end.DTO.MessageRequest;
import com.example.XDMHPL_Back_end.Services.MessageService;
import com.example.XDMHPL_Back_end.model.ChatBox;
import com.example.XDMHPL_Back_end.model.MessageMediaModel;
import com.example.XDMHPL_Back_end.model.MessageModel;

@Controller
public class MessageWebSocketController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public List<MessageMediaModel> convertMediaList(List<MessageMediaDTO> mediaDTOList) {
        return mediaDTOList.stream()
                .map(dto -> {
                    MessageMediaModel mediaModel = new MessageMediaModel();
                    mediaModel.setMediaType(dto.getMediaType());
                    mediaModel.setMediaURL(dto.getMediaURL());
                    return mediaModel;
                })
                .collect(Collectors.toList());
    }
    // Gửi tin nhắn qua WebSocket
    @MessageMapping("/chat/send")
    public void sendMessage(@Payload MessageRequest request) {
        try {
            System.out.println(request.getChatBoxId());
            System.out.println(request.getSenderId());
            System.out.println(request.getReceiverId());
            System.out.println(request.getText());
            System.out.println(request.getMediaList());
            List<MessageMediaModel> mediaModelList =  convertMediaList(request.getMediaList());
            // Gửi tin nhắn vào DB và trả về message
            MessageModel savedMessage = messageService.sendMessage(
                request.getSenderId(),
                request.getReceiverId(),
                request.getText(),
                request.getChatBoxId(),
                mediaModelList
            );

            System.out.println(savedMessage.getText());
            // Gửi tin nhắn qua WebSocket tới các client
            simpMessagingTemplate.convertAndSend("/topic/messages/"+request.getChatBoxId(), savedMessage);

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


