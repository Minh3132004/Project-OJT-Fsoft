package com.example.Blog_SpringBoot.repository;

import com.example.Blog_SpringBoot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsernameAndPasswordHash(String username , String passwordHash);
}
