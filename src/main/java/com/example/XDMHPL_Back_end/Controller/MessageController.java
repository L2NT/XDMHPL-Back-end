package com.example.XDMHPL_Back_end.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.XDMHPL_Back_end.Services.MessageService;
import com.example.XDMHPL_Back_end.model.MessageMediaModel;
import com.example.XDMHPL_Back_end.model.MessageModel;

@RestController
@RequestMapping("/messages")

public class MessageController {

    @Autowired
    private MessageService messageService;

    // Gửi tin nhắn REST
    @PostMapping("/send")
    public MessageModel sendMessage(
        @RequestParam Integer senderId,
        @RequestParam Integer receiverId,
        @RequestParam String text,
        @RequestParam Integer chatBoxId,
        @RequestBody(required = false) List<MessageMediaModel> mediaList) {

        // Debug log
        if (mediaList == null || mediaList.isEmpty()) {
            System.out.println("Tin nhắn văn bản: " + text);
        } else {
            System.out.println("Gửi media kèm theo: " + mediaList.size() + " file.");
        }

        // Lưu tin nhắn vào DB
        return messageService.sendMessage(senderId, receiverId, text, chatBoxId, mediaList);
    }

    // Lấy toàn bộ tin nhắn theo chatBoxId
    @GetMapping("/{chatBoxId}")
    public List<MessageModel> getMessagesByChatBox(@PathVariable Integer chatBoxId) {
        return messageService.getMessagesByChatBox(chatBoxId);
    }

    // // ✅ Lấy tin nhắn cuối cùng trong chatBox
    // @GetMapping("/last/{chatBoxId}")
    // public Message getLastMessageByChatBox(@PathVariable Integer chatBoxId) {
    //     return messageService.getLastMessageByChatBox(chatBoxId);
    // }

    // Đánh dấu tin nhắn là đã xem
    @PostMapping("/markAsSeen/{messageId}")
    public MessageModel markAsSeen(@PathVariable Integer messageId) {
        return messageService.markAsSeen(messageId);
    }
}
