package com.example.XDMHPL_Back_end.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.XDMHPL_Back_end.model.PostShare;

public interface PostShareRepository extends JpaRepository<PostShare, Integer> {
        Optional<PostShare> findById(Integer id);

}
