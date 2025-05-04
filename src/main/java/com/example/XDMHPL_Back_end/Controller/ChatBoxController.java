package com.example.XDMHPL_Back_end.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.XDMHPL_Back_end.Services.ChatBoxService;
import com.example.XDMHPL_Back_end.model.ChatBox;
import com.example.XDMHPL_Back_end.model.MessageMediaModel;

@RestController
@RequestMapping("/chat")
public class ChatBoxController {

    @Autowired
    private ChatBoxService chatBoxService;

    // API lấy thông tin chat box và người đang chat
    @GetMapping("/info/{chatBoxId}/{currentUserId}")
    public ChatBox getChatBoxInfo(@PathVariable Integer chatBoxId, @PathVariable Integer currentUserId) {
        return chatBoxService.getChatBoxInfo(chatBoxId, currentUserId);
    }

    @PostMapping("/update/{chatBoxId}")
    public ChatBox updateBoxChat(@PathVariable Integer chatBoxId, 
                                  @RequestParam String name, 
                                  @RequestParam(required = false) String imageUrl) {
        // Nếu không có imageUrl, sử dụng giá trị mặc định
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageUrl = "/assets/default-avatar.jpg";
        }
    
        // Gọi service để cập nhật thông tin
        return chatBoxService.updateBoxChat(chatBoxId, name, imageUrl);
    }
    


@GetMapping("/images/{chatBoxId}")
public List<MessageMediaModel> getChatBoxImages(@PathVariable Integer chatBoxId) {
    return chatBoxService.getChatBoxImages(chatBoxId);
}

@GetMapping("/sidebar/{userId}")
public ResponseEntity<List<ChatBox>> getUserSidebar(@PathVariable Integer userId) {
    return ResponseEntity.ok(chatBoxService.getSidebarChatBoxesByUserId(userId));
}

}

