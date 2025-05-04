package com.example.XDMHPL_Back_end.DTO;

<<<<<<< HEAD
import com.example.XDMHPL_Back_end.model.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "chatboxdetail")
public class ChatBoxDetailDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatboxdetailId")
    private Integer chatboxdetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ChatBoxID", nullable = false)
    private ChatBoxDTO chatBox;

    public ChatBoxDetailDTO() {}

    public ChatBoxDetailDTO(Users user, ChatBoxDTO chatBox) {
        this.user = user;
        this.chatBox = chatBox;
    }

    public Integer getChatboxdetailId() {
        return chatboxdetailId;
    }

    public void setChatboxdetailId(Integer chatboxdetailId) {
        this.chatboxdetailId = chatboxdetailId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public ChatBoxDTO getChatBox() {
        return chatBox;
    }

    public void setChatBox(ChatBoxDTO chatBox) {
        this.chatBox = chatBox;
    }
=======
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatBoxDetailDTO {
    private int userID;
>>>>>>> tuan
}
