package com.example.XDMHPL_Back_end.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "notification")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notificationID;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "SenderID")
    private Users sender;

    @Enumerated(EnumType.STRING) 
    @Column(name = "Type")
    private NotificationStatus type;

    @ManyToOne
    @JoinColumn(name = "PostID")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "CommentID")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "MessageID")
    private Message message;

    @Column(name = "Content")
    private String content;

    @Column(name = "CreatedAt")
    private Date createdAt;

    @Column(name = "IsReadFlag")
    private int isReadFlag;

}
