package com.example.Blog_SpringBoot.controller;

import com.example.Blog_SpringBoot.model.User;
import com.example.Blog_SpringBoot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class AuthController {

    private UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String displayName,
            @RequestParam String passwordHash,
            Model model) {
        
        // Validation
        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("error", "Username is required");
            model.addAttribute("user", new User());
            return "register";
        }
        
        if (displayName == null || displayName.trim().isEmpty()) {
            model.addAttribute("error", "Display name is required");
            model.addAttribute("user", new User());
            return "register";
        }
        
        if (passwordHash == null || passwordHash.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters");
            model.addAttribute("user", new User());
            return "register";
        }
        
        // Create user object
        User user = new User();
        user.setUsername(username.trim());
        user.setDisplayName(displayName.trim());
        user.setPasswordHash("{noop}" + passwordHash); // No encoding prefix
        user.setCreatedAt(LocalDateTime.now());
        
        // Try to add user
        User savedUser = userService.addUser(user);
        
        if (savedUser == null) {
            model.addAttribute("error", "Username already exists. Please choose a different username.");
            model.addAttribute("user", new User());
            return "register";
        }
        
        model.addAttribute("success", "Account created successfully! You can now login.");
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
}
