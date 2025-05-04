package com.example.XDMHPL_Back_end.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "chatbox")
@AllArgsConstructor
@Data
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

}
