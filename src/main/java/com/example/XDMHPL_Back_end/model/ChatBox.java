package com.example.XDMHPL_Back_end.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "chatbox")

@NoArgsConstructor

public class ChatBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chatBoxID;

    @Column(name = "ImageURL")
    private String imageURL;

    @Column(name = "ChatBoxName")
    private String chatBoxName;

    @Column(name = "Mute")
    private Boolean mute; // Kiểu dữ liệu Boolean

    @Column(name = "Block")
    private Boolean block; // Kiểu dữ liệu Boolean

    @Column(name = "IsGroup")
    private Boolean isGroup; // Kiểu dữ liệu Boolean

    @OneToMany(mappedBy = "chatBox", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChatBoxDetail> chatBoxDetails;

    @OneToMany(mappedBy = "chatBox", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MessageModel> messages;

    // Constructor with parameters
    public ChatBox(int chatBoxID, String chatBoxName, String imageURL, Boolean mute, Boolean block, Boolean isGroup) {
        this.chatBoxID = chatBoxID;
        this.chatBoxName = chatBoxName;
        this.imageURL = imageURL;
        this.mute = mute;
        this.block = block;
        this.isGroup = isGroup;
    }

    // Getter and Setter methods
    public int getChatBoxID() {
        return chatBoxID;
    }

    public void setChatBoxID(int chatBoxID) {
        this.chatBoxID = chatBoxID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getChatBoxName() {
        return chatBoxName;
    }

    public void setChatBoxName(String chatBoxName) {
        this.chatBoxName = chatBoxName;
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

    public List<ChatBoxDetail> getChatBoxDetails() {
        return chatBoxDetails;
    }

    public void setChatBoxDetails(List<ChatBoxDetail> chatBoxDetails) {
        this.chatBoxDetails = chatBoxDetails;
    }

    public List<MessageModel> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageModel> messages) {
        this.messages = messages;
    }
}
