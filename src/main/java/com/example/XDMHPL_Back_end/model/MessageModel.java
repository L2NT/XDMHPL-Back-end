package com.example.XDMHPL_Back_end.model;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "message")
public class MessageModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MessageID")
    private int messageId;

    @Column(name = "Text", length = 500)
    private String text;

    @Column(name = "Time")
    private LocalDateTime time;

    @Column(name = "Seen")
    private Boolean seen;

    @Column(name = "Display")
    private Boolean display;

    // Liên kết với ChatBox (ManyToOne)
    @ManyToOne
    @JoinColumn(name = "ChatBoxID", referencedColumnName = "chatBoxID")
    @JsonBackReference // Ngừng việc serialization ngược về ChatBox
    private ChatBox chatBox;

    // Liên kết ngược với MessageMedia (OneToMany)
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
    @JsonIgnore  // Bỏ qua mediaList trong quá trình serialization
    private List<MessageMediaModel> mediaList;

    // Getters & Setters
    public int getMessageId() {
        return messageId;
    }

    @JsonProperty("chatBoxId")
    public Integer getChatBoxId() {
        return chatBox != null ? chatBox.getChatBoxID() : null;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public ChatBox getChatBox() {
        return chatBox;
    }

    public void setChatBox(ChatBox chatBox) {
        this.chatBox = chatBox;
    }

    public List<MessageMediaModel> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<MessageMediaModel> mediaList) {
        this.mediaList = mediaList;
    }
}