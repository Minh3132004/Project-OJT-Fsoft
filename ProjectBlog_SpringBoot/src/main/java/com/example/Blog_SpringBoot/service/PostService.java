package com.example.Blog_SpringBoot.service;

import com.example.Blog_SpringBoot.model.Post;
import com.example.Blog_SpringBoot.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post addPost(Post post) {
        return postRepository.saveAndFlush(post);
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> findById(int id) {
        return postRepository.findById(id);
    }

    public Post update(Post post) {
        return postRepository.saveAndFlush(post);
    }
}
