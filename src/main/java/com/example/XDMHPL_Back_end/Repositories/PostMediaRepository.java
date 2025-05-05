package com.example.XDMHPL_Back_end.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.XDMHPL_Back_end.model.PostMedia;

@Repository
public interface PostMediaRepository extends JpaRepository<PostMedia, Integer>{
    Optional<PostMedia> findById(Integer id); 
    
}
