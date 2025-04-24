package com.example.XDMHPL_Back_end.model;

import java.sql.Date;
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
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int postID;

	@Column(name = "CreationDate", nullable = false, unique = false)
	private Date creationDate;

	@Column(name = "Type", nullable = false, unique = false)
	private String type;

	@Column(name = "UserID", nullable = false, unique = false)
	private int userID;

	@Column(name = "Content", nullable = false)
	private String content;
    
    @Column(name = "PriorityScore", nullable = false, unique = false)
    private int priorityScore;
    
    @Column(name = "Hide", nullable = false)
    private int hide;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostMedia> mediaList;

}
