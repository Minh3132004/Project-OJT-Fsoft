package com.example.Blog_SpringBoot.controller;

import com.example.Blog_SpringBoot.model.Post;
import com.example.Blog_SpringBoot.model.User;
import com.example.Blog_SpringBoot.service.PostService;
import com.example.Blog_SpringBoot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.security.core.Authentication;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;

@Controller
public class PostController {

    private PostService postService;
    private UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/posts/new")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new Post());
        return "posts/new";
    }

    @PostMapping("/posts")
    public String createPost(@Valid @ModelAttribute("post") Post post,
                             BindingResult bindingResult,
                             Authentication authentication,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "posts/new";
        }

        String username = authentication.getName();
        User user = userService.findByUsername(username);

        post.setAuthorId(user.getId());
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        postService.addPost(post);

        return "redirect:/";
    }
}
