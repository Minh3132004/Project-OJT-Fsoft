package com.example.Blog_SpringBoot.controller;

import com.example.Blog_SpringBoot.model.Comment;
import com.example.Blog_SpringBoot.model.Post;
import com.example.Blog_SpringBoot.service.CommentService;
import com.example.Blog_SpringBoot.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @GetMapping("/{id}")
    public String showCommentForm(@PathVariable("id") int id, Model model) {
        Optional<Post> optionalPost = postService.findById(id);
        if (optionalPost.isEmpty()) {
            return "redirect:/";
        }
        
        Post post = optionalPost.get();
        Comment comment = new Comment();

        model.addAttribute("post", post);
        model.addAttribute("comment", comment);

        return "comments/new";
    }

    @PostMapping("/{id}/add")
    public String createComment(@PathVariable("id") int id,
                               @Valid @ModelAttribute("comment") Comment comment,
                               BindingResult bindingResult,
                               Model model) {
        
        Optional<Post> optionalPost = postService.findById(id);
        if (optionalPost.isEmpty()) {
            return "redirect:/";
        }
        
        Post post = optionalPost.get();
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "comments/new";
        }
        
        comment.setId(0);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());
        
        commentService.addComment(comment);
        
        return "redirect:/";
    }
}