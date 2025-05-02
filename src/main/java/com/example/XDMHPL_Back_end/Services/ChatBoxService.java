package com.example.XDMHPL_Back_end.Services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.XDMHPL_Back_end.DTO.ChatBoxDTO;
import com.example.XDMHPL_Back_end.DTO.ChatBoxDetailDTO;
import com.example.XDMHPL_Back_end.DTO.ChatBoxInfo;
import com.example.XDMHPL_Back_end.DTO.MessageMediaDTO;
import com.example.XDMHPL_Back_end.Repositories.ChatBoxDetailRepository;
import com.example.XDMHPL_Back_end.Repositories.ChatBoxRepository;
import com.example.XDMHPL_Back_end.Repositories.MessageMediaRepository;
import com.example.XDMHPL_Back_end.Repositories.UserRepository;
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
    public ChatBoxInfo getChatBoxInfo(Integer chatBoxId, Integer currentUserId) {
        // Tìm chatBox theo ID
        ChatBoxDTO box = chatBoxRepo.findById(chatBoxId).orElse(null);
        if (box == null) return null;

        // Tìm danh sách thành viên trong ChatBox dựa trên ChatBoxDetail
        List<ChatBoxDetailDTO> members = chatBoxDetailRepo.findByChatBox_ChatBoxID(chatBoxId);
        if (members == null || members.isEmpty()) return null;
        
        // Tìm người dùng mục tiêu (người không phải là currentUser)
        Users targetUser = null;
        for (ChatBoxDetailDTO member : members) {
            Integer userId = member.getUser().getUserID();  // Lấy userId từ đối tượng User
            if (!userId.equals(currentUserId)) {
                targetUser = userRepo.findById(userId).orElse(null);
                break;
            }
        }

        if (targetUser == null) return null;

        // Trả về thông tin chat box và người dùng mục tiêu
        return new ChatBoxInfo(
                box.getChatBoxID(),
                box.getChatBoxName(),
                box.getImageURL(),
                targetUser.getFullName(),
                targetUser.getAvatar()
        );
    }

        public ChatBoxDTO updateBoxChat(Integer chatBoxId, String name, String imageUrl) {
            // Kiểm tra dữ liệu đầu vào
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("ChatBox name cannot be empty");
            }
            if (imageUrl == null || imageUrl.isEmpty()) {
                throw new IllegalArgumentException("Image URL cannot be empty");
            }
        
            // Tìm chatBox theo ID
            ChatBoxDTO box = chatBoxRepo.findById(chatBoxId).orElse(null);
        
        
            // Cập nhật tên và URL hình ảnh
            box.setChatBoxName(name);
            box.setImageURL(imageUrl);
            
            // Lưu lại và trả về đối tượng chatBox đã cập nhật
            return chatBoxRepo.save(box);
        }
    

  @Transactional
public List<MessageMediaDTO> getChatBoxImages(Integer chatBoxId) {
    List<MessageMediaModel> mediaList = mediaRepo.findMediaByChatBoxID(chatBoxId);

    return mediaList.stream()
        .map(media -> new MessageMediaDTO(
            media.getMessageMediaID(),
            media.getMediaType(),
            media.getMediaURL()
        ))
        .collect(Collectors.toList());
}

public List<ChatBoxDTO> getSidebarChatBoxesByUserId(Integer userId) {
    List<ChatBoxDTO> chatBoxes = chatBoxDetailRepo.findChatBoxesByUserId(userId);

    return chatBoxes.stream()
        .map(box -> new ChatBoxDTO(
            box.getChatBoxID(),
            box.getChatBoxName(),
            box.getImageURL(),
            box.getMute(),
            box.getBlock(),
            box.getIsGroup()
        ))
        .collect(Collectors.toList());
}



    
    
}