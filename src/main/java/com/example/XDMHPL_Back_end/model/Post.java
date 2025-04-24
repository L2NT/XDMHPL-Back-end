package com.example.XDMHPL_Back_end.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "post_type", discriminatorType = DiscriminatorType.STRING)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postID;
    
    @Column(name = "CreationDate", nullable = false)
    private Date creationDate;
    
	@ManyToOne
	@JoinColumn(name = "UserID")
	private Users user; // Thêm quan hệ với Users
    
    @Column(name = "Content", nullable = false)
    private String content;
    
    @Column(name = "PriorityScore", nullable = false)
    private int priorityScore;
    
    @Column(name = "Hide", nullable = false)
    private int hide;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostMedia> mediaList;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Like> likes;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;
}
