package com.example.Blog_SpringBoot.controller;

import com.example.Blog_SpringBoot.model.Post;
import com.example.Blog_SpringBoot.model.User;
import com.example.Blog_SpringBoot.service.PostService;
import com.example.Blog_SpringBoot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private PostService postService;
    private UserService userService;

    @Autowired
    public PostRestController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable int id) {
        Optional<Post> post = postService.findById(id);
        if (post.isPresent()) {
            return ResponseEntity.ok(post.get());
        }
        return ResponseEntity.notFound().build();
    }

    // POST /api/posts - Create new post
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody Post post, Authentication authentication) {
        if (authentication == null || authentication.getName() == null || authentication.getName().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        }

        User user = userService.findByUsername(authentication.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        post.setAuthorId(user.getId());
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        Post savedPost = postService.addPost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

    // PUT /api/posts/{id} - Update post
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable int id, 
                                       @Valid @RequestBody Post postUpdate, 
                                       Authentication authentication) {
        if (authentication == null || authentication.getName() == null || authentication.getName().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        }

        Optional<Post> optionalPost = postService.findById(id);
        if (optionalPost.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Post existingPost = optionalPost.get();
        User user = userService.findByUsername(authentication.getName());
        
        if (user == null || existingPost.getAuthorId() != user.getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only edit your own posts");
        }

        existingPost.setTitle(postUpdate.getTitle());
        existingPost.setContent(postUpdate.getContent());
        existingPost.setUpdatedAt(LocalDateTime.now());

        Post updatedPost = postService.update(existingPost);
        return ResponseEntity.ok(updatedPost);
    }

    // DELETE /api/posts/{id} - Delete post
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable int id, Authentication authentication) {
        if (authentication == null || authentication.getName() == null || authentication.getName().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        }

        Optional<Post> optionalPost = postService.findById(id);
        if (optionalPost.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Post post = optionalPost.get();
        User user = userService.findByUsername(authentication.getName());
        
        if (user == null || post.getAuthorId() != user.getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only delete your own posts");
        }

        postService.delete(post);
        return ResponseEntity.ok().body("Post deleted successfully");
    }
}

