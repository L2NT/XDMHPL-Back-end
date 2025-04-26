package com.example.XDMHPL_Back_end.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.XDMHPL_Back_end.model.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findById(Integer id);

    List<Post> findByHide(int hide);
}