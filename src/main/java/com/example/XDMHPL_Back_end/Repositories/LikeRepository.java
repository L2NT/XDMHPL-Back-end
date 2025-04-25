package com.example.XDMHPL_Back_end.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.XDMHPL_Back_end.model.Like;
import com.example.XDMHPL_Back_end.model.Post;
import com.example.XDMHPL_Back_end.model.Users;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    Like findByPostAndUser(Post post, Users user);
}