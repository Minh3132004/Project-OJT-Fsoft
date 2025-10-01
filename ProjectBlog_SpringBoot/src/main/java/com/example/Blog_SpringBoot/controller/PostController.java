package com.example.Blog_SpringBoot.controller;

import com.example.Blog_SpringBoot.model.Post;
import com.example.Blog_SpringBoot.model.User;
import com.example.Blog_SpringBoot.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }


}
