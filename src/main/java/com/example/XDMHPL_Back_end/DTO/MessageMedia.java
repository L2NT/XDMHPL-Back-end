package com.example.XDMHPL_Back_end.DTO;

import jakarta.persistence.*;

@Entity
@Table(name = "messagemedia")
public class MessageMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MessageMediaID")
    private Integer messageMediaID;

    @Column(name = "MediaType", length = 50)
    private String mediaType;

    @Column(name = "MediaURL", length = 200)
    private String mediaURL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MessageID")
    private Message message;

    public MessageMedia() {}

    public MessageMedia(Integer messageMediaID, String mediaType, String mediaURL, Message message) {
        this.messageMediaID = messageMediaID;
        this.mediaType = mediaType;
        this.mediaURL = mediaURL;
        this.message = message;
    }

    public Integer getMessageMediaID() {
        return messageMediaID;
    }

    public void setMessageMediaID(Integer messageMediaID) {
        this.messageMediaID = messageMediaID;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
