package com.example.Blog_SpringBoot.service;

import com.example.Blog_SpringBoot.model.Comment;
import com.example.Blog_SpringBoot.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment addComment(Comment comment) {
        return commentRepository.saveAndFlush(comment);
    }
}
