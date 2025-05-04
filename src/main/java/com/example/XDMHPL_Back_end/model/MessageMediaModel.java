package com.example.XDMHPL_Back_end.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
 @Table(name = "messagemedia")
 public class MessageMediaModel {
 
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private int messageMediaID;
 
     @Column(name = "MediaType")
     private String mediaType;
 
     @Column(name = "MediaURL")
     private String mediaURL;
 
     @ManyToOne
     @JoinColumn(name = "MessageID")
     private MessageModel message;
 

     public MessageMediaModel(int messageMediaID, String mediaType, String mediaURL, MessageModel message) {
        this.messageMediaID = messageMediaID;
        this.mediaType = mediaType;
        this.mediaURL = mediaURL;
        this.message = message;
    }
     public int getMessageMediaID() {
         return messageMediaID;
     }
 
     public void setMessageMediaID(int messageMediaID) {
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
 
     public MessageModel getMessage() {
         return message;
     }
 
     public void setMessage(MessageModel message) {
         this.message = message;
     }
 }