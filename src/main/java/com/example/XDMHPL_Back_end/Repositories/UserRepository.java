package com.example.XDMHPL_Back_end.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.XDMHPL_Back_end.model.Users;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findById(Integer id);
    Users findByEmail(String email);
    Users findByUserName(String userName);
    Users findByPhoneNumber(String phoneNumber);
    public List<Users> findByHideFalse();
    
    @Query("SELECT u.userID, u.fullName FROM Users u WHERE u.userID = :userID")
    Object[] findUserInfoByID(@Param("userID") int userID);
}