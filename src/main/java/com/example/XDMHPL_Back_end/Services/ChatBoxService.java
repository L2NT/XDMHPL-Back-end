package com.example.XDMHPL_Back_end.Services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

