package com.example.Blog_SpringBoot.controller;

import com.example.Blog_SpringBoot.model.User;
import com.example.Blog_SpringBoot.service.PostService;
import com.example.Blog_SpringBoot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private PostService postService;
    private UserService userService;

    @Autowired
    public HomeController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        model.addAttribute("posts", postService.getAllPosts());

        if (authentication != null && authentication.isAuthenticated() &&
                authentication.getName() != null && !"anonymousUser".equals(authentication.getName())) {
            User current = userService.findByUsername(authentication.getName());
            if (current != null) {
                model.addAttribute("currentUserId", current.getId());
            }
        }

        return "index";
    }
}
