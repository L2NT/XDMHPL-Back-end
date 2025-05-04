package com.example.XDMHPL_Back_end.Services;

<<<<<<< HEAD
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


import com.example.XDMHPL_Back_end.Repositories.ChatBoxDetailRepository;
=======
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

>>>>>>> tuan
import com.example.XDMHPL_Back_end.Repositories.ChatBoxRepository;
import com.example.XDMHPL_Back_end.Repositories.MessageMediaRepository;
import com.example.XDMHPL_Back_end.Repositories.MessageRepository;
import com.example.XDMHPL_Back_end.model.ChatBox;
<<<<<<< HEAD
import com.example.XDMHPL_Back_end.model.MessageMediaModel;

import com.example.XDMHPL_Back_end.model.MessageModel;

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

    public MessageModel sendMessage(Integer senderId, Integer chatBoxId, String text, Integer chatBoxId2, List<MessageMediaModel> mediaList) {
        Optional<ChatBox> chatBoxOptional = chatBoxRepository.findById(chatBoxId);
        if (!chatBoxOptional.isPresent()) {
            throw new RuntimeException("ChatBox không tồn tại.");
        }

        ChatBox chatBox = chatBoxOptional.get();
    
        // Kiểm tra người gửi có phải thành viên của cuộc trò chuyện không

        if (!chatBox.getIsGroup() && !isUserInChatBox(senderId, chatBoxId)) {
            throw new RuntimeException("Người gửi không phải là thành viên của cuộc trò chuyện.");
        }
    
        // Nếu không có text nhưng có media thì đặt nội dung mặc định
        if ((text == null || text.trim().isEmpty()) && mediaList != null && !mediaList.isEmpty()) {
            text = "Gửi tin nhắn hình ảnh";
        }
    
        // Tạo tin nhắn
        MessageModel message = new MessageModel();
        message.setText(text);
        message.setTime(LocalDateTime.now());
        message.setSeen(false);
        message.setDisplay(true);
        message.setChatBox(chatBox);
    
        // Lưu tin nhắn
        MessageModel savedMessage = messageRepository.save(message);
    
        // Lưu media nếu có
        saveMessageMedia(mediaList, savedMessage);
    
        // Gửi realtime
        sendRealTimeMessage(senderId, savedMessage, chatBox);
    
        return savedMessage;
    }


    
   

    
    private void saveMessageMedia(List<MessageMediaModel> mediaList, MessageModel savedMessage) {
        if (mediaList != null && !mediaList.isEmpty()) {
            for (MessageMediaModel media : mediaList) {
                String fileUrl = media.getMediaURL(); // URL đầy đủ từ client

                String mediaType = media.getMediaType();
                String fileName = fileUrl.substring(fileUrl.lastIndexOf("_") + 1);

    
                // 👉 Chỉ lưu tên file vào DB (hoặc đường dẫn `/assets/` nếu cần)
                String imageUrl = "http://localhost:8080/assets/" + fileName;
                if (mediaType == null || mediaType.isEmpty()) {
                    mediaType = getMediaTypeFromFileUrl(fileUrl);
                }
    
                // Gán lại giá trị sau khi xử lý
                media.setMediaURL(imageUrl); // hoặc chỉ `fileName` nếu bạn dùng path cố định từ FE
                media.setMediaType(mediaType);
                media.setMessage(savedMessage);
    
                messageMediaRepository.save(media);
            }
        }
    }

    


    // Phương thức gửi tin nhắn realtime
   

    
    private void sendRealTimeMessage(Integer senderId, MessageModel savedMessage, ChatBox chatBox) {
        // Gửi tin nhắn realtime cho người gửi

        messagingTemplate.convertAndSendToUser(String.valueOf(senderId), "/queue/messages", savedMessage);
    
        // Gửi tin nhắn realtime cho người trong cuộc trò chuyện
        if (!chatBox.getIsGroup()) {
            messagingTemplate.convertAndSendToUser(String.valueOf(chatBox.getChatBoxID()), "/queue/messages", savedMessage);
        } else {
            messagingTemplate.convertAndSend("/topic/chatbox/" + chatBox.getChatBoxID(), savedMessage);
        }
    }
    

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
        return "application/octet-stream";
    }

    private boolean isUserInChatBox(Integer userId, Integer chatBoxId) {
        return chatBoxDetailRepository.existsByUser_UserIDAndChatBox_ChatBoxID(userId, chatBoxId);
    }

    public List<MessageModel> getMessagesByChatBox(Integer chatBoxId) {
        return messageRepository.findByChatBox_ChatBoxID(chatBoxId);
    }

    public MessageModel markAsSeen(Integer messageId) {
        Optional<MessageModel> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isPresent()) {
            MessageModel message = messageOpt.get();
            message.setSeen(true);
            return messageRepository.save(message);
        }
        return null;
    }
}
            
            
=======
import com.example.XDMHPL_Back_end.model.Message;
import com.example.XDMHPL_Back_end.model.MessageMedia;

import jakarta.transaction.Transactional;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private MessageMediaRepository messageMediaRepository;
    
    @Autowired
    private ChatBoxRepository chatBoxRepository;

    /**
     * Tạo và lưu tin nhắn mới (chỉ text)
     */
    @Transactional
    public Message createMessage(int chatBoxId, String text) {
        // Lấy thông tin chatbox
        ChatBox chatBox = chatBoxRepository.findById(chatBoxId)
            .orElseThrow(() -> new RuntimeException("ChatBox not found with id: " + chatBoxId));
        
        // Tạo tin nhắn mới
        Message message = new Message();
        message.setChatBox(chatBox);
        message.setText(text);
        message.setTime(new Date());
        message.setSeen(0); // Chưa đọc
        message.setDisplay(1); // Hiển thị
        message.setMediaList(new ArrayList<>());
        
        return messageRepository.save(message);
    }

    /**
     * Tạo tin nhắn kèm media
     */
    @Transactional
    public Message createMessageWithMedia(int chatBoxId, String text, List<MessageMedia> media) {
        // Tạo tin nhắn cơ bản trước
        Message message = createMessage(chatBoxId, text);
        
        // Thêm các media vào tin nhắn
        for (MessageMedia mediaItem : media) {
            mediaItem.setMessage(message);
            messageMediaRepository.save(mediaItem);
        }
        
        // Cập nhật lại danh sách media
        message.setMediaList(messageMediaRepository.findByMessageMessageID(message.getMessageID()));
        
        return message;
    }

    /**
     * Cập nhật trạng thái đã đọc của tin nhắn
     */
    @Transactional
    public Message markMessageAsSeen(int messageId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new RuntimeException("Message not found with id: " + messageId));
        
        message.setSeen(1); // Đánh dấu đã đọc
        return messageRepository.save(message);
    }

    /**
     * Ẩn tin nhắn (xóa mềm)
     */
    @Transactional
    public Message hideMessage(int messageId) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new RuntimeException("Message not found with id: " + messageId));
        
        message.setDisplay(0); // Ẩn tin nhắn
        return messageRepository.save(message);
    }

    /**
     * Lấy tất cả tin nhắn của một chatbox
     */
    public List<Message> getChatBoxMessages(int chatBoxId) {
        return messageRepository.findByChatBoxChatBoxIDOrderByTimeAsc(chatBoxId);
    }

    /**
     * Lấy tin nhắn chưa đọc trong một chatbox
     */
    public List<Message> getUnreadMessages(int chatBoxId) {
        return messageRepository.findByChatBoxChatBoxIDAndSeen(chatBoxId, 0);
    }
    
    /**
     * Lấy tất cả tin nhắn giữa hai người dùng
     */
    public List<Message> getMessagesBetweenUsers(int userId1, int userId2) {
        return messageRepository.findMessagesBetweenUsers(userId1, userId2);
    }
    
    /**
     * Đánh dấu tất cả tin nhắn trong chatbox là đã đọc
     */
    @Transactional
    public void markAllMessagesAsRead(int chatBoxId, int userId) {
        List<Message> unreadMessages = messageRepository.findByChatBoxChatBoxIDAndSeen(chatBoxId, 0);
        for (Message message : unreadMessages) {
            message.setSeen(1);
            messageRepository.save(message);
        }
    }
}
>>>>>>> tuan
