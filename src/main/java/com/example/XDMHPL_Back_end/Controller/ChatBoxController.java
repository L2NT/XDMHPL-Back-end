package com.example.XDMHPL_Back_end.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.XDMHPL_Back_end.DTO.ChatBoxDTO;
import com.example.XDMHPL_Back_end.DTO.PostDTO;
import com.example.XDMHPL_Back_end.Services.ChatBoxService;
import com.example.XDMHPL_Back_end.Services.MessageService;
import com.example.XDMHPL_Back_end.model.ChatBox;
@RestController
@RequestMapping("/chatbox")
public class ChatboxController {
    @Autowired
    private ChatBoxService chatBoxService;

    @Autowired
    private MessageService messageService;

    /**
     * Lấy tất cả chatbox của một người dùng
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserChatboxes(@PathVariable int userId) {
        System.out.println("User " + userId + " requested chatboxes list");
        try {
            List<ChatBox> chatBoxes = chatBoxService.getUserChatboxes(userId);
            return ResponseEntity.ok(chatBoxes.stream().map(ChatBoxDTO::fromEntity).toList());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Tạo chatbox mới giữa hai người dùng
     */
    @PostMapping("/create")
    public ResponseEntity<?> createChatBox(@RequestBody Map<String, Integer> request) {
        try {
            int userId1 = request.get("userId1");
            int userId2 = request.get("userId2");

            ChatBox chatBox = chatBoxService.createChatBox(userId1, userId2);

            return new ResponseEntity<>(ChatBoxDTO.fromEntity(chatBox), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Tạo chatbox nhóm
     */
    @PostMapping("/create-group")
    public ResponseEntity<?> createGroupChatBox(@RequestBody Map<String, Object> request) {
        try {
            String chatBoxName = (String) request.get("chatBoxName");
            List<Integer> userIds = (List<Integer>) request.get("userIds");

            // Triển khai logic tạo chatbox nhóm ở đây
            // (Mở rộng ChatBoxService để hỗ trợ tạo nhóm)

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Group chat created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Cập nhật thông tin chatbox
     */
    // @PutMapping("/{chatBoxId}")
    // public ResponseEntity<?> updateChatBox(
    //         @PathVariable int chatBoxId,
    //         @RequestBody Map<String, Object> request) {
    //     try {
    //         ChatBox chatBox = chatBoxService.getChatBoxById(chatBoxId)
    //                 .orElseThrow(() -> new RuntimeException("ChatBox not found with id: " + chatBoxId));

    //         // Cập nhật các thông tin
    //         if (request.containsKey("chatBoxName")) {
    //             chatBox.setChatBoxName((String) request.get("chatBoxName"));
    //         }

    //         if (request.containsKey("imageURL")) {
    //             chatBox.setImageURL((String) request.get("imageURL"));
    //         }

    //         if (request.containsKey("mute")) {
    //             chatBox.setMute((Boolean) request.get("mute"));
    //         }

    //         if (request.containsKey("block")) {
    //             chatBox.setBlock((Boolean) request.get("block"));
    //         }

    //         ChatBox updatedChatBox = chatBoxService.updateChatBox(chatBox);

    //         return ResponseEntity.ok(updatedChatBox);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                 .body("Error: " + e.getMessage());
    //     }
    // }

    /**
     * Thêm người dùng vào chatbox (chỉ dành cho nhóm)
     */
    @PostMapping("/{chatBoxId}/add-user")
    public ResponseEntity<?> addUserToChatBox(
            @PathVariable int chatBoxId,
            @RequestBody Map<String, Integer> request) {
        try {
            int userId = request.get("userId");

            // Triển khai logic thêm người dùng ở đây
            // (Mở rộng ChatBoxService để hỗ trợ thêm người dùng vào nhóm)

            return ResponseEntity.ok(Map.of("message", "User added to chatbox successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Xóa người dùng khỏi chatbox (chỉ dành cho nhóm)
     */
    @DeleteMapping("/{chatBoxId}/remove-user/{userId}")
    public ResponseEntity<?> removeUserFromChatBox(
            @PathVariable int chatBoxId,
            @PathVariable int userId) {
        try {
            // Triển khai logic xóa người dùng ở đây
            // (Mở rộng ChatBoxService để hỗ trợ xóa người dùng khỏi nhóm)

            return ResponseEntity.ok(Map.of("message", "User removed from chatbox successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Thoát khỏi chatbox (người dùng tự xóa mình khỏi chatbox)
     */
    @DeleteMapping("/{chatBoxId}/leave/{userId}")
    public ResponseEntity<?> leaveChatBox(
            @PathVariable int chatBoxId,
            @PathVariable int userId) {
        try {
            // Triển khai logic thoát khỏi chatbox ở đây
            // (Mở rộng ChatBoxService để hỗ trợ thoát khỏi nhóm)

            return ResponseEntity.ok(Map.of("message", "Left chatbox successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
