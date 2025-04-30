package com.example.XDMHPL_Back_end.DTO;

import java.util.List;

public class MessageRequest {
    private Integer senderId;
    private Integer receiverId;
    private String text;
    private Integer chatBoxId;
    private List<MessageMedia> mediaList;

    // Getters and Setters
    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getChatBoxId() {
        return chatBoxId;
    }

    public void setChatBoxId(Integer chatBoxId) {
        this.chatBoxId = chatBoxId;
    }

    public List<MessageMedia> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<MessageMedia> mediaList) {
        this.mediaList = mediaList;
    }
}
