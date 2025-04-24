package com.example.XDMHPL_Back_end.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chatbox")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chatBoxID;

    @Column(name = "ImageURL")
    private String imageURL;

    @Column(name = "ChatBoxName")
    private String chatBoxName;

    @Column(name = "Mute")
    private int mute;

    @Column(name = "Block")
    private int block;

    @OneToMany(mappedBy = "chatBox", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChatBoxDetail> chatBoxDetails;

    @OneToMany(mappedBy = "chatBox", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages;
}
