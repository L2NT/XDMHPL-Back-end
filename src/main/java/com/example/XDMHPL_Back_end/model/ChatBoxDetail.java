package com.example.XDMHPL_Back_end.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chatboxdetail")
public class ChatBoxDetail {
    @EmbeddedId
    private ChatBoxDetailId id;

    @ManyToOne
    @MapsId("userID")
    @JoinColumn(name = "UserID")
    private Users user;

    @ManyToOne
    @MapsId("chatBoxID")
    @JoinColumn(name = "ChatBoxID")
    private ChatBox chatBox;

}


