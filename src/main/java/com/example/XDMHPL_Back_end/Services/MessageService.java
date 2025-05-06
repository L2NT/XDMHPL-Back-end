package com.example.XDMHPL_Back_end.Services;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.XDMHPL_Back_end.DTO.MessageDTO;
import com.example.XDMHPL_Back_end.Repositories.ChatBoxDetailRepository;

import com.example.XDMHPL_Back_end.Repositories.ChatBoxRepository;
import com.example.XDMHPL_Back_end.Repositories.MessageMediaRepository;
import com.example.XDMHPL_Back_end.Repositories.MessageRepository;
import com.example.XDMHPL_Back_end.Repositories.UserRepository;
import com.example.XDMHPL_Back_end.model.ChatBox;
import com.example.XDMHPL_Back_end.model.MessageMediaModel;

import com.example.XDMHPL_Back_end.model.MessageModel;
import com.example.XDMHPL_Back_end.model.Users;

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

    @Autowired
    private UserRepository usersRepository;

    public MessageModel sendMessage(Integer senderId, Integer chatBoxId, String text, Integer chatBoxId2, List<MessageMediaModel> mediaList) {
        Optional<ChatBox> chatBoxOptional = chatBoxRepository.findById(chatBoxId);
        Optional<Users> user = usersRepository.findById(senderId);

        if (!chatBoxOptional.isPresent()) {
            throw new RuntimeException("ChatBox không tồn tại.");
        }

        if (!user.isPresent()) {
            throw new RuntimeException("User không tồn tại.");
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
        message.setUsers(user.get());
    
        // Lưu tin nhắn
        MessageModel savedMessage = messageRepository.save(message);
    
        System.out.println("Message saved with ID: " + savedMessage.getMessageId());
        // Lưu media nếu có
        saveMessageMedia(mediaList, savedMessage);
    
        // Gửi realtime
        // sendRealTimeMessage(senderId, savedMessage, chatBox);
    
        return savedMessage;
    }


    
   

    
    private void saveMessageMedia(List<MessageMediaModel> mediaList, MessageModel savedMessage) {
        if (mediaList != null && !mediaList.isEmpty()) {
            List<MessageMediaModel> savedMediaList = new ArrayList<>();
            for (MessageMediaModel media : mediaList) {
                String fileUrl = media.getMediaURL(); // URL đầy đủ từ client

                String mediaType = media.getMediaType();
                String fileName = fileUrl;

    
                // 👉 Chỉ lưu tên file vào DB (hoặc đường dẫn `/assets/` nếu cần)
                String imageUrl =fileName;
                if (mediaType == null || mediaType.isEmpty()) {
                    mediaType = getMediaTypeFromFileUrl(fileUrl);
                }
    
                // Gán lại giá trị sau khi xử lý
                media.setMediaURL(imageUrl); // hoặc chỉ `fileName` nếu bạn dùng path cố định từ FE
                media.setMediaType(mediaType);
                media.setMessage(savedMessage);
    
                MessageMediaModel savedMedia = messageMediaRepository.save(media);
                savedMediaList.add(savedMedia);
            }

             // Gán lại danh sách media vào savedMessage
            savedMessage.setMediaList(savedMediaList);
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

    public MessageModel updateMessage (MessageDTO message) {
        MessageModel message1 = messageRepository.findById(message.getMessageId()).get();
        message1.setText(message.getText());
        return messageRepository.save(message1);
    }

    public MessageModel deleteMessage(Integer messageId) {
        Optional<MessageModel> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isPresent()) {
            MessageModel message = messageOpt.get();
            message.setDisplay(false);
            return messageRepository.save(message);
        }
        return null;
    }
}
