package com.example.XDMHPL_Back_end.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.XDMHPL_Back_end.model.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {

}
