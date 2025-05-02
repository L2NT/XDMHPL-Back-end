package com.example.XDMHPL_Back_end.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "messagemedia")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageMedia {
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
}
