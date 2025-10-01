package org.example.dao;

import org.example.model.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findByUsername(String username);
    User insert(User user);
}



