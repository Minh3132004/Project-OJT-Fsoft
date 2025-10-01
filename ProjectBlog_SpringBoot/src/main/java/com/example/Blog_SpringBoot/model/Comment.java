package com.example.Blog_SpringBoot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "author_name")
    @NotBlank(message = "Author name is required")
    @Size(min = 2, max = 50, message = "Author name must be between 2 and 50 characters")
    private String authorName;

    @NotBlank(message = "Comment content is required")
    @Size(min = 5, max = 1000, message = "Comment must be between 5 and 1000 characters")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Comment() {
    }

    public Comment(int id, Post post, String authorName, String content, LocalDateTime createdAt) {
        this.id = id;
        this.post = post;
        this.authorName = authorName;
        this.content = content;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }
    
    // Convenience method to get post ID
    public int getPostId() { 
        return post != null ? post.getId() : 0; 
    }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", postId=" + getPostId() +
                ", authorName='" + authorName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}



