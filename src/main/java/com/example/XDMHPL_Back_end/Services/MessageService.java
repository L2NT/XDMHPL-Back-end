package com.example.XDMHPL_Back_end.Services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.XDMHPL_Back_end.Repositories.ChatBoxRepository;
import com.example.XDMHPL_Back_end.Repositories.MessageMediaRepository;
import com.example.XDMHPL_Back_end.Repositories.MessageRepository;
import com.example.XDMHPL_Back_end.model.ChatBox;
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
