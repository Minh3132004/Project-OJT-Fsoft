package org.example.dao;

import org.example.model.Comment;

import java.util.List;

public interface CommentDao {
    Comment insert(Comment comment);
    boolean deleteById(int id);
    List<Comment> findByPostId(int postId);
}



