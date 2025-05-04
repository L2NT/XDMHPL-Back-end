package com.example.XDMHPL_Back_end.Services;

import java.util.List;
<<<<<<< HEAD
=======
import java.util.Optional;
>>>>>>> tuan
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.XDMHPL_Back_end.Repositories.ChatBoxDetailRepository;
import com.example.XDMHPL_Back_end.Repositories.ChatBoxRepository;
<<<<<<< HEAD
import com.example.XDMHPL_Back_end.Repositories.MessageMediaRepository;
import com.example.XDMHPL_Back_end.Repositories.UserRepository;
import com.example.XDMHPL_Back_end.model.ChatBox;
import com.example.XDMHPL_Back_end.model.ChatBoxDetail;
import com.example.XDMHPL_Back_end.model.MessageMediaModel;
=======
import com.example.XDMHPL_Back_end.Repositories.UserRepository;
import com.example.XDMHPL_Back_end.model.ChatBox;
import com.example.XDMHPL_Back_end.model.ChatBoxDetail;
import com.example.XDMHPL_Back_end.model.ChatBoxDetailId;
>>>>>>> tuan
import com.example.XDMHPL_Back_end.model.Users;

import jakarta.transaction.Transactional;

@Service
public class ChatBoxService {
<<<<<<< HEAD

    @Autowired
    private ChatBoxRepository chatBoxRepo;

    @Autowired
    private ChatBoxDetailRepository chatBoxDetailRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MessageMediaRepository mediaRepo;

    // API lấy thông tin chat box và người đang chat
    public ChatBox getChatBoxInfo(Integer chatBoxId, Integer currentUserId) {
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
        return new ChatBox(
            box.getChatBoxID(),
            box.getChatBoxName(),
            box.getImageURL(),
            box.getMute(),      // Boolean
            box.getBlock(),     // Boolean
            box.getIsGroup()    // Boolean
        );
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
    public List<ChatBox> getSidebarChatBoxesByUserId(Integer userId) {
        // Tìm các chi tiết ChatBox liên quan đến User
        List<ChatBoxDetail> chatBoxDetails = chatBoxDetailRepo.findByChatBox_ChatBoxID(userId);
        
        // Trả về danh sách ChatBox từ các chi tiết
        return chatBoxDetails.stream()
            .map(detail -> detail.getChatBox()) // Lấy ChatBox từ ChatBoxDetail
            .map(box -> new ChatBox(
                box.getChatBoxID(),
                box.getChatBoxName(),
                box.getImageURL(),
                box.getMute(),      // Boolean
                box.getBlock(),     // Boolean
                box.getIsGroup()    // Boolean
            ))
            .collect(Collectors.toList());
    }
}

=======
    
    @Autowired
    private ChatBoxRepository chatBoxRepository;
    
    @Autowired
    private ChatBoxDetailRepository chatBoxDetailRepository;
    
    @Autowired
    private UserRepository usersRepository;

    /**
     * Lấy tất cả chatbox của một người dùng
     */
    public List<ChatBox> getUserChatboxes(int userId) {
        // Lấy tất cả ChatBoxDetail của user
        List<ChatBoxDetail> chatBoxDetails = chatBoxDetailRepository.findByIdUserID(userId);
        
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
                    .map(detail -> detail.getId().getUserID())
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
        Users user1 = usersRepository.findById(userId1)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId1));
        Users user2 = usersRepository.findById(userId2)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId2));
        
        // Tạo chatbox mới
        ChatBox chatBox = new ChatBox();
        chatBox.setChatBoxName(user1.getFullName() + ", " + user2.getFullName());
        chatBox.setImageURL(null); // Có thể dùng avatar của người kia
        chatBox.setMute(0);
        chatBox.setBlock(0);
        
        // Lưu chatbox
        ChatBox savedChatBox = chatBoxRepository.save(chatBox);
        
        // Tạo ChatBoxDetail cho người dùng 1
        ChatBoxDetailId id1 = new ChatBoxDetailId(userId1, savedChatBox.getChatBoxID());
        ChatBoxDetail detail1 = new ChatBoxDetail();
        detail1.setId(id1);
        detail1.setUser(user1);
        detail1.setChatBox(savedChatBox);
        chatBoxDetailRepository.save(detail1);
        
        // Tạo ChatBoxDetail cho người dùng 2
        ChatBoxDetailId id2 = new ChatBoxDetailId(userId2, savedChatBox.getChatBoxID());
        ChatBoxDetail detail2 = new ChatBoxDetail();
        detail2.setId(id2);
        detail2.setUser(user2);
        detail2.setChatBox(savedChatBox);
        chatBoxDetailRepository.save(detail2);
        
        return savedChatBox;
    }

    /**
     * Lấy thông tin người tham gia trong chatbox
     */
    public List<Users> getChatBoxParticipants(int chatBoxId) {
        ChatBox chatBox = chatBoxRepository.findById(chatBoxId)
            .orElseThrow(() -> new RuntimeException("ChatBox not found with id: " + chatBoxId));
            
        return chatBox.getChatBoxDetails().stream()
            .map(ChatBoxDetail::getUser)
            .collect(Collectors.toList());
    }
    
    /**
     * Kiểm tra xem người dùng có trong chatbox không
     */
    public boolean isUserInChatBox(int userId, int chatBoxId) {
        return chatBoxDetailRepository.findByIdUserIDAndIdChatBoxID(userId, chatBoxId).isPresent();
    }


    public Optional<ChatBox> getChatBoxById(int chatBoxId) {
        return chatBoxRepository.findById(chatBoxId);
    }
    
    /**
     * Cập nhật thông tin chatbox
     */
    @Transactional
    public ChatBox updateChatBox(ChatBox chatBox) {
        return chatBoxRepository.save(chatBox);
    }
    
    /**
     * Thêm người dùng vào chatbox nhóm
     */
    @Transactional
    public ChatBoxDetail addUserToChatBox(int userId, int chatBoxId) {
        // Kiểm tra xem người dùng đã tồn tại trong chatbox chưa
        Optional<ChatBoxDetail> existingDetail = chatBoxDetailRepository.findByIdUserIDAndIdChatBoxID(userId, chatBoxId);
        if (existingDetail.isPresent()) {
            return existingDetail.get();
        }
        
        // Lấy thông tin người dùng
        Users user = usersRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        // Lấy thông tin chatbox
        ChatBox chatBox = chatBoxRepository.findById(chatBoxId)
            .orElseThrow(() -> new RuntimeException("ChatBox not found with id: " + chatBoxId));
        
        // Tạo ChatBoxDetail mới
        ChatBoxDetailId id = new ChatBoxDetailId(userId, chatBoxId);
        ChatBoxDetail detail = new ChatBoxDetail();
        detail.setId(id);
        detail.setUser(user);
        detail.setChatBox(chatBox);
        
        return chatBoxDetailRepository.save(detail);
    }
    
    /**
     * Xóa người dùng khỏi chatbox nhóm
     */
    @Transactional
    public void removeUserFromChatBox(int userId, int chatBoxId) {
        // Kiểm tra xem người dùng có trong chatbox không
        ChatBoxDetailId id = new ChatBoxDetailId(userId, chatBoxId);
        chatBoxDetailRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User is not a member of this chatbox"));
        
        // Xóa khỏi chatbox
        chatBoxDetailRepository.deleteById(id);
    }
    
    /**
     * Tạo chatbox nhóm mới
     */
    @Transactional
    public ChatBox createGroupChatBox(String chatBoxName, List<Integer> userIds) {
        if (userIds.size() < 2) {
            throw new RuntimeException("A group chat must have at least 2 members");
        }
        
        // Tạo chatbox mới
        ChatBox chatBox = new ChatBox();
        chatBox.setChatBoxName(chatBoxName);
        chatBox.setImageURL(null);
        chatBox.setMute(0);
        chatBox.setBlock(0);
        
        // Lưu chatbox
        ChatBox savedChatBox = chatBoxRepository.save(chatBox);
        
        // Thêm các người dùng vào chatbox
        for (Integer userId : userIds) {
            Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
            
            ChatBoxDetailId detailId = new ChatBoxDetailId(userId, savedChatBox.getChatBoxID());
            ChatBoxDetail detail = new ChatBoxDetail();
            detail.setId(detailId);
            detail.setUser(user);
            detail.setChatBox(savedChatBox);
            chatBoxDetailRepository.save(detail);
        }
        
        return savedChatBox;
    }
}
>>>>>>> tuan
