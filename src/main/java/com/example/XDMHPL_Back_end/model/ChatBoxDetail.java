package com.example.XDMHPL_Back_end.model;

import jakarta.persistence.*;

@Entity
@Table(name = "chatboxdetail")
public class ChatBoxDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatboxdetailId")
    private Integer chatBoxDetailId;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "ChatBoxID", nullable = false)
    private ChatBox chatBox;

    // Constructors
    public ChatBoxDetail() {
    }

    public ChatBoxDetail(Integer chatBoxDetailId, Users user, ChatBox chatBox) {
        this.chatBoxDetailId = chatBoxDetailId;
        this.user = user;
        this.chatBox = chatBox;
    }

    // Getters and Setters
    public Integer getChatBoxDetailId() {
        return chatBoxDetailId;
    }

    public void setChatBoxDetailId(Integer chatBoxDetailId) {
        this.chatBoxDetailId = chatBoxDetailId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public ChatBox getChatBox() {
        return chatBox;
    }

    public void setChatBox(ChatBox chatBox) {
        this.chatBox = chatBox;
    }
}
