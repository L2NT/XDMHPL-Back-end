package com.example.XDMHPL_Back_end.DTO;

public class ChatBoxInfo {
    private int chatBoxID;
    private String chatBoxName;
    private String chatBoxImage;
    private String otherUserName;
    private String otherUserAvatar;

    public ChatBoxInfo() {}

    public ChatBoxInfo(int chatBoxID, String chatBoxName, String chatBoxImage,
                       String otherUserName, String otherUserAvatar) {
        this.chatBoxID = chatBoxID;
        this.chatBoxName = chatBoxName;
        this.chatBoxImage = chatBoxImage;
        this.otherUserName = otherUserName;
        this.otherUserAvatar = otherUserAvatar;
    }

    // Getters and Setters
    public int getChatBoxID() {
        return chatBoxID;
    }

    public void setChatBoxID(int chatBoxID) {
        this.chatBoxID = chatBoxID;
    }

    public String getChatBoxName() {
        return chatBoxName;
    }

    public void setChatBoxName(String chatBoxName) {
        this.chatBoxName = chatBoxName;
    }

    public String getChatBoxImage() {
        return chatBoxImage;
    }

    public void setChatBoxImage(String chatBoxImage) {
        this.chatBoxImage = chatBoxImage;
    }

    public String getOtherUserName() {
        return otherUserName;
    }

    public void setOtherUserName(String otherUserName) {
        this.otherUserName = otherUserName;
    }

    public String getOtherUserAvatar() {
        return otherUserAvatar;
    }

    public void setOtherUserAvatar(String otherUserAvatar) {
        this.otherUserAvatar = otherUserAvatar;
    }
}

