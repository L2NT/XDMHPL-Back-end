package com.example.XDMHPL_Back_end.DTO;

<<<<<<< HEAD
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "chatbox")
public class ChatBoxDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chatBoxID;

    @Column(name = "ChatBoxName", length = 50)
    private String chatBoxName;

    @Column(name = "ImageURL", length = 200)
    private String imageURL;

    @Column(name = "Mute")
    private Boolean mute;

    @Column(name = "Block")
    private Boolean block;

    @Column(name = "isGroup", nullable = false)
    private Boolean isGroup;

    // Constructor mặc định
    public ChatBoxDTO() {
    }

    // Constructor đầy đủ
    public ChatBoxDTO(Integer chatBoxID, String chatBoxName, String imageURL, Boolean mute, Boolean block, Boolean isGroup) {
        this.chatBoxID = chatBoxID;
        this.chatBoxName = chatBoxName;
        this.imageURL = imageURL;
        this.mute = mute;
        this.block = block;
        this.isGroup = isGroup;
    }

    // Getter và Setter
    public Integer getChatBoxID() {
        return chatBoxID;
    }

    public void setChatBoxID(Integer chatBoxID) {
        this.chatBoxID = chatBoxID;
    }

    public String getChatBoxName() {
        return chatBoxName;
    }

    public void setChatBoxName(String chatBoxName) {
        this.chatBoxName = chatBoxName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Boolean getMute() {
        return mute;
    }

    public void setMute(Boolean mute) {
        this.mute = mute;
    }

    public Boolean getBlock() {
        return block;
    }

    public void setBlock(Boolean block) {
        this.block = block;
    }

    public Boolean getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(Boolean isGroup) {
        this.isGroup = isGroup;
    }
}
=======
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.XDMHPL_Back_end.model.ChatBox;
import com.example.XDMHPL_Back_end.model.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatBoxDTO {
    private int chatBoxID;
    private String imageURL;
    private String chatBoxName;
    private int mute;
    private int block;
    private List<ChatBoxDetailDTO> chatBoxDetails;

    public static ChatBoxDTO fromEntity(ChatBox chatBox)
    {
        ChatBoxDTO chatBoxDTO = new ChatBoxDTO();
        chatBoxDTO.setChatBoxID(chatBox.getChatBoxID());
        chatBoxDTO.setImageURL(chatBox.getImageURL());
        chatBoxDTO.setChatBoxName(chatBox.getChatBoxName());
        chatBoxDTO.setMute(chatBox.getMute());
        chatBoxDTO.setBlock(chatBox.getBlock());

        // Chuyển đổi danh sách ChatBoxDetail sang ChatBoxDetailDTO
        if (chatBox.getChatBoxDetails() != null) {
            List<ChatBoxDetailDTO> chatBoxDetailDTOs = chatBox.getChatBoxDetails().stream()
                .map(detail -> new ChatBoxDetailDTO(detail.getId().getUserID()))
                .collect(Collectors.toList());
            chatBoxDTO.setChatBoxDetails(chatBoxDetailDTOs);
        } else {
            chatBoxDTO.setChatBoxDetails(new ArrayList<>());
        }

        return chatBoxDTO;
    }
}
>>>>>>> tuan
