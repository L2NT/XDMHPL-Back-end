package com.example.XDMHPL_Back_end.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "postmedia")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostMedia {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postMediaID;

    @Column(name = "Type")
    private String type;

    @Column(name = "MediaURL")
    private String mediaURL;

    @ManyToOne
    @JoinColumn(name = "PostID")
    private Post post;
}
