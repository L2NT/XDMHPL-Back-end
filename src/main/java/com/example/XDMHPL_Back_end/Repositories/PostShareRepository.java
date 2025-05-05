package com.example.XDMHPL_Back_end.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.XDMHPL_Back_end.model.PostShare;

@Repository
public interface PostShareRepository extends JpaRepository<PostShare, Integer> {
        Optional<PostShare> findById(Integer id);

}
