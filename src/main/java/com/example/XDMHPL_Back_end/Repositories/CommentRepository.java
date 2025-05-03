package com.example.XDMHPL_Back_end.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.XDMHPL_Back_end.model.Comment;
import com.example.XDMHPL_Back_end.model.Post;
import com.example.XDMHPL_Back_end.model.Users;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Comment findByPostAndUser(Post post, Users user);
    
    Optional<Comment> findByCommentID(int commentID);
}
