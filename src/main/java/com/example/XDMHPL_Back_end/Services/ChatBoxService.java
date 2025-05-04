package com.example.XDMHPL_Back_end.Services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.XDMHPL_Back_end.DTO.ChatBoxDTO;
import com.example.XDMHPL_Back_end.Repositories.ChatBoxDetailRepository;
import com.example.XDMHPL_Back_end.Repositories.ChatBoxRepository;
import com.example.XDMHPL_Back_end.Repositories.MessageMediaRepository;
import com.example.XDMHPL_Back_end.Repositories.UserRepository;
import com.example.XDMHPL_Back_end.model.ChatBox;
import com.example.XDMHPL_Back_end.model.ChatBoxDetail;
import com.example.XDMHPL_Back_end.model.MessageMediaModel;
import com.example.XDMHPL_Back_end.model.Users;

import jakarta.transaction.Transactional;

@Service
public class ChatBoxService {

    @Autowired
    private ChatBoxRepository chatBoxRepo;

    @Autowired
    private ChatBoxDetailRepository chatBoxDetailRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MessageMediaRepository mediaRepo;


    public Optional<ChatBox> getChatBoxById(int chatBoxId) {
        return chatBoxRepo.findById(chatBoxId);
    }

    // API lấy thông tin chat box và người đang chat
    public ChatBoxDTO getChatBoxInfo(Integer chatBoxId, Integer currentUserId) {
        // Tìm chatBox theo ID
        ChatBox box = chatBoxRepo.findById(chatBoxId).orElse(null);
        if (box == null) {
            return null; // Trả về null nếu không tìm thấy ChatBox
        }

        // Tìm danh sách thành viên trong ChatBox dựa trên ChatBoxDetail
        List<ChatBoxDetail> members = chatBoxDetailRepo.findByChatBox_ChatBoxID(chatBoxId);
        if (members == null || members.isEmpty()) {
            return null; // Trả về null nếu không tìm thấy thành viên nào
        }
        
        // Tìm người dùng mục tiêu (người không phải là currentUser)
        Users targetUser = null;
        for (ChatBoxDetail member : members) {
            Integer userId = member.getUser().getUserID();  // Lấy userId từ đối tượng User
            if (!userId.equals(currentUserId)) {
                targetUser = userRepo.findById(userId).orElse(null);
                break;
            }
        }

        if (targetUser == null) {
            return null; // Trả về null nếu không tìm thấy người dùng mục tiêu
        }

        // Trả về thông tin chat box và người dùng mục tiêu
        return ChatBoxDTO.fromEntity(box);
    }

    // Cập nhật ChatBox
    public ChatBox updateBoxChat(Integer chatBoxId, String name, String imageUrl) {
        // Kiểm tra dữ liệu đầu vào
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("ChatBox name cannot be empty");
        }
        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new IllegalArgumentException("Image URL cannot be empty");
        }

        // Tìm chatBox theo ID
        ChatBox box = chatBoxRepo.findById(chatBoxId).orElse(null);
        if (box == null) {
            throw new IllegalArgumentException("ChatBox not found with id: " + chatBoxId);
        }

        // Cập nhật tên và URL hình ảnh
        box.setChatBoxName(name);
        box.setImageURL(imageUrl);

        // Lưu lại và trả về đối tượng chatBox đã cập nhật
        return chatBoxRepo.save(box);
    }

    // API lấy danh sách media của ChatBox
    @Transactional
    public List<MessageMediaModel> getChatBoxImages(Integer chatBoxId) {
        List<MessageMediaModel> mediaList = mediaRepo.findMediaByChatBoxID(chatBoxId);
        return mediaList.stream()
            .map(media -> new MessageMediaModel(
                media.getMessageMediaID(),
                media.getMediaType(),
                media.getMediaURL(),
                media.getMessage()  // bổ sung messageID
            ))
            .collect(Collectors.toList());
    }

    // API lấy danh sách ChatBox theo UserId
    public List<ChatBoxDTO> getSidebarChatBoxesByUserId(Integer userId) {
        // Tìm các chi tiết ChatBox liên quan đến User
        List<ChatBoxDetail> chatBoxDetails = chatBoxDetailRepo.findByChatBox_ChatBoxID(userId);
        
        // Trả về danh sách ChatBox từ các chi tiết
        return chatBoxDetails.stream()
            .map(detail -> detail.getChatBox()) // Lấy ChatBox từ ChatBoxDetail
            .map(box -> ChatBoxDTO.fromEntity(box))
            .collect(Collectors.toList());
    }


    public List<ChatBox> getUserChatboxes(int userId) {
        // Lấy tất cả ChatBoxDetail của user
        List<ChatBoxDetail> chatBoxDetails = chatBoxDetailRepo.findByUser_UserID(userId);
        
        // Lấy các ChatBox tương ứng
        return chatBoxDetails.stream()
            .map(ChatBoxDetail::getChatBox)
            .collect(Collectors.toList());
    }


      /**
     * Tìm chatbox giữa hai người dùng
     */
    public Optional<ChatBox> findChatBoxBetweenUsers(int userId1, int userId2) {
        // Lấy tất cả chatbox của userId1
        List<ChatBox> user1ChatBoxes = getUserChatboxes(userId1);
        
        // Lọc các chatbox mà cả hai user đều tham gia
        return user1ChatBoxes.stream()
            .filter(chatBox -> {
                List<Integer> participantIds = chatBox.getChatBoxDetails().stream()
                    .map(detail -> detail.getUser().getUserID())
                    .collect(Collectors.toList());
                
                // Kiểm tra nếu chatbox chỉ có 2 người và có userId2
                return participantIds.size() == 2 && participantIds.contains(userId2);
            })
            .findFirst();
    }

    /**
     * Tạo chatbox mới giữa hai người dùng
     */
    @Transactional
    public ChatBox createChatBox(int userId1, int userId2) {
        // Kiểm tra xem đã có chatbox giữa hai người chưa
        Optional<ChatBox> existingChatBox = findChatBoxBetweenUsers(userId1, userId2);
        if (existingChatBox.isPresent()) {
            return existingChatBox.get();
        }
        
        // Lấy thông tin người dùng
        Users user1 = userRepo.findById(userId1)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId1));
        Users user2 = userRepo.findById(userId2)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId2));
        
        // Tạo chatbox mới
        ChatBox chatBox = new ChatBox();
        chatBox.setChatBoxName(user1.getFullName() + ", " + user2.getFullName());
        chatBox.setImageURL(null); // Có thể dùng avatar của người kia
        chatBox.setMute(false);
        chatBox.setBlock(false);
        chatBox.setIsGroup(false);
        
        // Lưu chatbox
        ChatBox savedChatBox = chatBoxRepo.save(chatBox);
        

        ChatBoxDetail detail1 = new ChatBoxDetail();
        detail1.setUser(user1);
        detail1.setChatBox(savedChatBox);
        chatBoxDetailRepo.save(detail1);
        
        // Tạo ChatBoxDetail cho người dùng 2
        ChatBoxDetail detail2 = new ChatBoxDetail();
        detail2.setUser(user2);
        detail2.setChatBox(savedChatBox);
        chatBoxDetailRepo.save(detail2);
        
        return savedChatBox;
    }

    /**
     * Lấy thông tin người tham gia trong chatbox
     */
    public List<Users> getChatBoxParticipants(int chatBoxId) {
        ChatBox chatBox = chatBoxRepo.findById(chatBoxId)
            .orElseThrow(() -> new RuntimeException("ChatBox not found with id: " + chatBoxId));
            
        return chatBox.getChatBoxDetails().stream()
            .map(ChatBoxDetail::getUser)
            .collect(Collectors.toList());
    }
    
}
