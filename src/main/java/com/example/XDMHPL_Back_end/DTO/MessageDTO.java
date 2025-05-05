package com.example.XDMHPL_Back_end.DTO;

import java.time.LocalDateTime;
import java.util.List;

import com.example.XDMHPL_Back_end.model.MessageModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private int messageId;
    private String text;
    private LocalDateTime time;
    private Boolean seen;
    private Boolean display;
    private int chatBoxId;
    private int userId;
    private List<MessageMediaDTO> mediaList;

    public static MessageDTO toDTO(MessageModel message) {
        MessageDTO dto = new MessageDTO();
        dto.setMessageId(message.getMessageId());
        dto.setText(message.getText());
        dto.setTime(message.getTime());
        dto.setSeen(message.getSeen());
        dto.setDisplay(message.getDisplay());
        dto.setChatBoxId(message.getChatBox().getChatBoxID());
        dto.setUserId(message.getUsers().getUserID());
        if (message.getMediaList() != null) {
            List<MessageMediaDTO> mediaDTOs = message.getMediaList().stream().map(media -> {
                MessageMediaDTO mediaDTO = new MessageMediaDTO();
                mediaDTO.setMessageMediaID(media.getMessageMediaID());
                mediaDTO.setMediaType(media.getMediaType());
                mediaDTO.setMediaURL(media.getMediaURL());
                return mediaDTO;
            }).toList();
            dto.setMediaList(mediaDTOs);
        }

        return dto;
    }
}