package com.example.XDMHPL_Back_end.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.XDMHPL_Back_end.model.PostMedia;

public interface PostMediaRepository extends JpaRepository<PostMedia, Integer>{
    Optional<PostMedia> findById(Integer id); 
    
}
