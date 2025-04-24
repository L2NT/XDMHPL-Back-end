package com.example.XDMHPL_Back_end.model;

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
@Table(name = "likes")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int likeID;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "PostID")
    private Post post;

    // Constructors, getters, setters
}