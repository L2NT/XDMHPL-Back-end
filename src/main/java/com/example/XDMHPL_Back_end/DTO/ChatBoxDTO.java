package com.example.XDMHPL_Back_end.DTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.XDMHPL_Back_end.model.ChatBox;

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
    private boolean mute;
    private boolean block;
    private boolean isGroup;
    private List<ChatBoxDetailDTO> chatBoxDetails;

    public static ChatBoxDTO fromEntity(ChatBox chatBox)
    {
        ChatBoxDTO chatBoxDTO = new ChatBoxDTO();
        chatBoxDTO.setChatBoxID(chatBox.getChatBoxID());
        chatBoxDTO.setImageURL(chatBox.getImageURL());
        chatBoxDTO.setChatBoxName(chatBox.getChatBoxName());
        chatBoxDTO.setMute(chatBox.getMute());
        chatBoxDTO.setBlock(chatBox.getBlock());
        chatBoxDTO.setGroup(chatBox.getIsGroup());

        // Chuyển đổi danh sách ChatBoxDetail sang ChatBoxDetailDTO
        if (chatBox.getChatBoxDetails() != null) {
            List<ChatBoxDetailDTO> chatBoxDetailDTOs = chatBox.getChatBoxDetails().stream()
                .map(detail -> new ChatBoxDetailDTO(detail.getUser().getUserID()))
                .collect(Collectors.toList());
            chatBoxDTO.setChatBoxDetails(chatBoxDetailDTOs);
        } else {
            chatBoxDTO.setChatBoxDetails(new ArrayList<>());
        }

        return chatBoxDTO;
    }
}