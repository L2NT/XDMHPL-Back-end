package com.example.XDMHPL_Back_end.model;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "message")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int messageID;

    @Column(name = "Text")
    private String text;

    @Column(name = "Time")
    private Date time;

    @Column(name = "Seen")
    private int seen;

    @Column(name = "Display")
    private int display;

    @ManyToOne
    @JoinColumn(name = "ChatBoxID")
    private ChatBox chatBox;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MessageMedia> mediaList;
}
