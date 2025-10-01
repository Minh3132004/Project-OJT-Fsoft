package org.example.dao;

import org.example.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostDao {
    Post insert(Post post);
    boolean update(Post post);
    boolean deleteById(int id);
    Optional<Post> findById(int id);
    List<Post> findAll();
    Optional<Post> findByIdWithComments(int id);
    List<Post> searchByKeyword(String keyword);
}



