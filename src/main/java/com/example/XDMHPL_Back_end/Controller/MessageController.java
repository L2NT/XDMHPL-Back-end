package com.example.XDMHPL_Back_end.Controller;

<<<<<<< HEAD
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
=======
import java.io.IOException;
import java.util.ArrayList;
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
>>>>>>> tuan
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
<<<<<<< HEAD

import com.example.XDMHPL_Back_end.DTO.Message;

import com.example.XDMHPL_Back_end.DTO.MessageRequest;
import com.example.XDMHPL_Back_end.Services.MessageService;
import com.example.XDMHPL_Back_end.model.MessageMediaModel;
import com.example.XDMHPL_Back_end.model.MessageModel;

@RestController
@RequestMapping("/messages")
=======
import org.springframework.web.multipart.MultipartFile;

import com.example.XDMHPL_Back_end.Services.ChatBoxService;
import com.example.XDMHPL_Back_end.Services.MessageService;
import com.example.XDMHPL_Back_end.model.Message;
import com.example.XDMHPL_Back_end.model.MessageMedia;

@RestController
@RequestMapping("/api/message")
>>>>>>> tuan
public class MessageController {

    @Autowired
    private MessageService messageService;

<<<<<<< HEAD
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
=======
    @Autowired
    private ChatBoxService chatBoxService;

    /**
     * Lấy tin nhắn của một chatbox
     */
    @GetMapping("/chatbox/{chatBoxId}")
    public ResponseEntity<?> getChatBoxMessages(
            @PathVariable int chatBoxId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size) {
        try {
            // Lấy tất cả tin nhắn
            List<Message> messages = messageService.getChatBoxMessages(chatBoxId);

            // Phân trang (đơn giản)
            int start = Math.max(0, messages.size() - (page + 1) * size);
            int end = Math.max(0, messages.size() - page * size);
            List<Message> pagedMessages = messages.subList(start, end);

            // Chuyển đổi thành format hữu ích cho frontend
            List<Map<String, Object>> result = pagedMessages.stream().map(message -> {
                List<Map<String, Object>> mediaList = message.getMediaList().stream()
                        .map(media -> {
                            Map<String, Object> mediaMap = new HashMap<>();
                            mediaMap.put("mediaType", media.getMediaType());
                            mediaMap.put("mediaURL", media.getMediaURL());
                            return mediaMap;
                        })
                        .collect(Collectors.toList());

                Map<String, Object> messageMap = new HashMap<>();
                messageMap.put("messageID", message.getMessageID());
                messageMap.put("text", message.getText());
                messageMap.put("time", message.getTime());
                messageMap.put("seen", message.getSeen());
                messageMap.put("media", mediaList);
                return messageMap;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "messages", result,
                    "totalMessages", messages.size(),
                    "page", page,
                    "size", size));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Gửi tin nhắn mới (chỉ text)
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, Object> request) {
        try {
            int chatBoxId = (Integer) request.get("chatBoxId");
            String text = (String) request.get("text");

            // Kiểm tra xem người dùng có trong chatbox không
            int userId = (Integer) request.get("userId");
            if (!chatBoxService.isUserInChatBox(userId, chatBoxId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("User is not a member of this chatbox");
            }

            Message message = messageService.createMessage(chatBoxId, text);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "messageID", message.getMessageID(),
                            "text", message.getText(),
                            "time", message.getTime(),
                            "seen", message.getSeen()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Gửi tin nhắn kèm media
     */
    @PostMapping("/send-with-media")
    public ResponseEntity<?> sendMessageWithMedia(
            @RequestParam("chatBoxId") int chatBoxId,
            @RequestParam("text") String text,
            @RequestParam("userId") int userId,
            @RequestParam("files") List<MultipartFile> files) {
        try {
            // Kiểm tra xem người dùng có trong chatbox không
            if (!chatBoxService.isUserInChatBox(userId, chatBoxId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("User is not a member of this chatbox");
            }

            // Xử lý tải lên các file
            List<MessageMedia> mediaList = new ArrayList<>();
            for (MultipartFile file : files) {
                // Xác định loại media
                String mediaType = determineMediaType(file.getContentType());

                // Tạo tên file duy nhất
                String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                // Lưu file và lấy URL (giả định)
                String mediaURL = saveFile(file, filename);

                // Tạo đối tượng MessageMedia
                MessageMedia media = new MessageMedia();
                media.setMediaType(mediaType);
                media.setMediaURL(mediaURL);

                mediaList.add(media);
            }

            // Tạo tin nhắn với media
            Message message = messageService.createMessageWithMedia(chatBoxId, text, mediaList);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "messageID", message.getMessageID(),
                            "text", message.getText(),
                            "time", message.getTime(),
                            "seen", message.getSeen(),
                            "media", message.getMediaList().stream()
                                    .map(media -> Map.of(
                                            "mediaType", media.getMediaType(),
                                            "mediaURL", media.getMediaURL()))
                                    .collect(Collectors.toList())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Xác định loại media từ MIME type
     */
    private String determineMediaType(String contentType) {
        if (contentType == null)
            return "file";

        if (contentType.startsWith("image/")) {
            return "image";
        } else if (contentType.startsWith("video/")) {
            return "video";
        } else if (contentType.startsWith("audio/")) {
            return "audio";
        } else {
            return "file";
        }
    }

    /**
     * Lưu file và trả về URL
     * (Phương thức này cần được triển khai theo cách lưu trữ thực tế - ví dụ: local
     * storage, S3, v.v.)
     */
    private String saveFile(MultipartFile file, String filename) throws IOException {
        // Đây là phương thức giả định - trong thực tế, bạn sẽ thực hiện lưu file và trả
        // về URL

        // Ví dụ: nếu sử dụng local storage
        // Path path = Paths.get("uploads/" + filename);
        // Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // Trả về URL giả định
        return "/uploads/" + filename;
    }

    /**
     * Đánh dấu tin nhắn đã đọc
     */
    @PutMapping("/{messageId}/mark-as-seen")
    public ResponseEntity<?> markMessageAsSeen(@PathVariable int messageId) {
        try {
            Message message = messageService.markMessageAsSeen(messageId);

            return ResponseEntity.ok(Map.of(
                    "messageID", message.getMessageID(),
                    "seen", message.getSeen()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Đánh dấu tất cả tin nhắn trong chatbox là đã đọc
     */
    @PutMapping("/chatbox/{chatBoxId}/mark-all-seen")
    public ResponseEntity<?> markAllMessagesAsSeen(
            @PathVariable int chatBoxId,
            @RequestParam("userId") int userId) {
        try {
            // Kiểm tra xem người dùng có trong chatbox không
            if (!chatBoxService.isUserInChatBox(userId, chatBoxId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("User is not a member of this chatbox");
            }

            messageService.markAllMessagesAsRead(chatBoxId, userId);

            return ResponseEntity.ok(Map.of("message", "All messages marked as seen"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * Xóa (ẩn) tin nhắn
     */
    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable int messageId) {
        try {
            Message message = messageService.hideMessage(messageId);

            return ResponseEntity.ok(Map.of(
                    "messageID", message.getMessageID(),
                    "display", message.getDisplay()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
>>>>>>> tuan
