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
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.security.core.Authentication;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.Optional;

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

    @GetMapping("/posts/{id}")
    public String viewPost(@PathVariable("id") int id, Model model, Authentication authentication) {
        Optional<Post> optionalPost = postService.findById(id);
        if (optionalPost.isEmpty()) {
            return "redirect:/";
        }
        Post post = optionalPost.get();
        
        model.addAttribute("post", post);
        return "posts/detail";
    }

    @GetMapping("/posts/{id}/edit")
    public String editPost(@PathVariable("id") int id,
                           Authentication authentication,
                           Model model) {
        Optional<Post> optionalPost = postService.findById(id);
        if (optionalPost.isEmpty()) {
            return "redirect:/";
        }
        Post post = optionalPost.get();

        String username;
        if (authentication != null) {
            username = authentication.getName();
        } else {
            username = null;
        }
        if (username == null) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(username);
        if (user == null || post.getAuthorId() != user.getId()) {
            return "redirect:/";
        }

        model.addAttribute("post", post);
        return "posts/edit";
    }

    @PostMapping("/posts/{id}/edit")
    public String updatePost(@PathVariable("id") int id,
                             @Valid @ModelAttribute("post") Post form,
                             BindingResult bindingResult,
                             Authentication authentication,
                             Model model) {
        Optional<Post> optionalPost = postService.findById(id);
        if (optionalPost.isEmpty()) {
            return "redirect:/";
        }
        Post existing = optionalPost.get();

        String username;
        if (authentication != null) {
            username = authentication.getName();
        } else {
            username = null;
        }
        if (username == null) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(username);
        if (user == null || existing.getAuthorId() != user.getId()) {
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("post", form);
            return "posts/edit";
        }

        existing.setTitle(form.getTitle());
        existing.setContent(form.getContent());
        existing.setUpdatedAt(LocalDateTime.now());
        postService.update(existing);
        return "redirect:/";
    }

    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable("id") int id,
                             Authentication authentication) {
        Optional<Post> optionalPost = postService.findById(id);
        if (optionalPost.isEmpty()) {
            return "redirect:/";
        }
        Post post = optionalPost.get();

        String username;
        if (authentication != null) {
            username = authentication.getName();
        } else {
            username = null;
        }
        if (username == null) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(username);
        if (user == null || post.getAuthorId() != user.getId()) {
            return "redirect:/";
        }

        postService.delete(post);
        return "redirect:/";
    }
}
