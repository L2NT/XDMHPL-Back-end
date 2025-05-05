package com.example.XDMHPL_Back_end.DTO;

import java.util.List;
import java.util.stream.Collectors;

import com.example.XDMHPL_Back_end.model.MessageMediaModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    private Integer senderId;
    private Integer receiverId;
    private String text;
    private Integer chatBoxId;
    private List<MessageMediaDTO> mediaList;

    public List<MessageMediaModel> convertMediaList(List<MessageMediaDTO> mediaDTOList) {
    return mediaDTOList.stream()
            .map(dto -> {
                MessageMediaModel mediaModel = new MessageMediaModel();
                mediaModel.setMediaType(dto.getMediaType());
                mediaModel.setMediaURL(dto.getMediaURL());
                return mediaModel;
            })
            .collect(Collectors.toList());
}

}
