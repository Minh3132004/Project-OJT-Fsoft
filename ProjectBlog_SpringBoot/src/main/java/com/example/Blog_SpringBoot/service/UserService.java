package com.example.Blog_SpringBoot.service;

import com.example.Blog_SpringBoot.model.User;
import com.example.Blog_SpringBoot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        if(userRepository.findByUsername(user.getUsername()) != null) {
            return null;
        }
        return userRepository.saveAndFlush(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
