package com.example.XDMHPL_Back_end.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.XDMHPL_Back_end.DTO.ChatBox;
import com.example.XDMHPL_Back_end.DTO.Message;
import com.example.XDMHPL_Back_end.DTO.MessageMedia;
import com.example.XDMHPL_Back_end.Repositories.ChatBoxDetailRepository;
import com.example.XDMHPL_Back_end.Repositories.ChatBoxRepository;
import com.example.XDMHPL_Back_end.Repositories.MessageMediaRepository;
import com.example.XDMHPL_Back_end.Repositories.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatBoxRepository chatBoxRepository;

    @Autowired
    private MessageMediaRepository messageMediaRepository;

    @Autowired
    private ChatBoxDetailRepository chatBoxDetailRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Phương thức gửi tin nhắn (Cho nhóm hoặc 1-1)
    public Message sendMessage(Integer senderId, Integer chatBoxId, String text, Integer chatBoxId2, List<MessageMedia> mediaList) {
        // Tìm ChatBox
        Optional<ChatBox> chatBoxOptional = chatBoxRepository.findById(chatBoxId);
        if (!chatBoxOptional.isPresent()) {
            throw new RuntimeException("ChatBox không tồn tại.");
        }

        ChatBox chatBox = chatBoxOptional.get();

        // Kiểm tra nếu là cuộc trò chuyện 1-1 và người gửi có quyền gửi tin nhắn
        if (!chatBox.getIsGroup() && !isUserInChatBox(senderId, chatBoxId)) {
            throw new RuntimeException("Người gửi không phải là thành viên của cuộc trò chuyện.");
        }

        // Tạo tin nhắn mới
        Message message = new Message();
        message.setText(text);
        message.setTime(LocalDateTime.now());
        message.setSeen(false);
        message.setDisplay(true);
        message.setChatBox(chatBox);

        Message savedMessage = messageRepository.save(message);

        // Xử lý media nếu có
        saveMessageMedia(mediaList, savedMessage);

        // Gửi tin nhắn realtime
        sendRealTimeMessage(senderId, savedMessage, chatBox);

        return savedMessage;
    }

    private void saveMessageMedia(List<MessageMedia> mediaList, Message savedMessage) {
        if (mediaList != null && !mediaList.isEmpty()) {
            for (MessageMedia media : mediaList) {
                String fileUrl = media.getMediaURL();
                String mediaType = media.getMediaType();
    
                // Log kiểm tra dữ liệu
                System.out.println("File URL: " + fileUrl);
                System.out.println("Media Type: " + mediaType);
    
                if (fileUrl == null || fileUrl.isEmpty()) {
                    throw new RuntimeException("mediaUrl không hợp lệ.");
                }
    
                if (mediaType == null || mediaType.isEmpty()) {
                    mediaType = getMediaTypeFromFileUrl(fileUrl);
                }
    
                media.setMediaURL(fileUrl);
                media.setMediaType(mediaType);
                media.setMessage(savedMessage);
                
                // Log trước khi lưu
                System.out.println("Saving Media: " + media);
    
                messageMediaRepository.save(media);
            }
        }
    }
    

    // Phương thức gửi tin nhắn realtime
    private void sendRealTimeMessage(Integer senderId, Message savedMessage, ChatBox chatBox) {
        messagingTemplate.convertAndSendToUser(String.valueOf(senderId), "/queue/messages", savedMessage);

        if (!chatBox.getIsGroup()) {
            // Nếu không phải nhóm, gửi cho người nhận
            messagingTemplate.convertAndSendToUser(String.valueOf(chatBox.getChatBoxID()), "/queue/messages", savedMessage);
        } else {
            // Gửi cho tất cả thành viên trong nhóm
            messagingTemplate.convertAndSend("/topic/chatbox/" + chatBox.getChatBoxID(), savedMessage);
        }
    }

    // Phương thức xác định loại media dựa trên URL
    private String getMediaTypeFromFileUrl(String fileUrl) {
        if (fileUrl.endsWith(".png")) {
            return "image/png";
        } else if (fileUrl.endsWith(".jpg") || fileUrl.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileUrl.endsWith(".mp4")) {
            return "video/mp4";
        } else if (fileUrl.endsWith(".mp3")) {
            return "audio/mpeg";
        }
        return "application/octet-stream"; // Default
    }

    // Kiểm tra người dùng có nằm trong ChatBox không
    private boolean isUserInChatBox(Integer userId, Integer chatBoxId) {
        return chatBoxDetailRepository.existsByUser_UserIDAndChatBox_ChatBoxID(userId, chatBoxId);
    }

    // Lấy tin nhắn trong ChatBox
    public List<Message> getMessagesByChatBox(Integer chatBoxId) {
        // Lấy tất cả tin nhắn từ chatBox mà không bị lặp dữ liệu media
        List<Message> messages = messageRepository.findByChatBox_ChatBoxID(chatBoxId);
        // Có thể thêm xử lý nếu cần thiết, như kiểm tra media trong các message, v.v.
        return messages;
    }
    

    // Đánh dấu tin nhắn đã đọc
    public Message markAsSeen(Integer messageId) {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isPresent()) {
            Message message = messageOpt.get();
            message.setSeen(true);
            return messageRepository.save(message);
        }
        return null;
    }
}

