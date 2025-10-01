package org.example.service;

import org.example.dao.UserDao;
import org.example.dao.PostDao;
import org.example.dao.CommentDao;
import org.example.dao.jdbc.JdbcUserDao;
import org.example.dao.jdbc.JdbcPostDao;
import org.example.dao.jdbc.JdbcCommentDao;
import org.example.model.User;
import org.example.model.Post;
import org.example.model.Comment;


import java.util.List;
import java.util.Optional;

public class JdbcBlogService {
    private UserDao userDao;
    private PostDao postDao;
    private CommentDao commentDao;
    
    public JdbcBlogService() {
        this.userDao = new JdbcUserDao();
        this.postDao = new JdbcPostDao();
        this.commentDao = new JdbcCommentDao();
    }
    
    // ========== USER MANAGEMENT ==========
    
    public void addUser(User user) {
        userDao.insert(user);
    }
    
    public Optional<User> findUserByUsername(String username) {
        return userDao.findByUsername(username);
    }
    
    public Optional<User> authenticateUser(String username, String password) {
        return ((JdbcUserDao) userDao).findByUsernameAndPassword(username, password);
    }
    
    // ========== POST MANAGEMENT ==========
    
    public void addPost(Post post) {
        postDao.insert(post);
    }
    
    public List<Post> getAllPosts() {
        List<Post> posts = postDao.findAll();
        // Đồng bộ comments cho tất cả posts
        for (Post post : posts) {
            List<Comment> comments = commentDao.findByPostId(post.getId());
            post.setComments(comments);
        }
        return posts;
    }

    public List<Post> searchPosts(String keyword) {
        List<Post> posts = postDao.searchByKeyword(keyword);
        for (Post post : posts) {
            List<Comment> comments = commentDao.findByPostId(post.getId());
            post.setComments(comments);
        }
        return posts;
    }
    
    public Optional<Post> getPostById(int id) {
        // Sử dụng JOIN để lấy post và comments trong một query
        return postDao.findByIdWithComments(id);
    }
    
    public boolean updatePost(Post post) {
        return postDao.update(post);
    }
    
    public boolean deletePost(int id) {
        return postDao.deleteById(id);
    }
    
    // ========== COMMENT MANAGEMENT ==========
    
    public boolean addComment(Comment comment) {
        // Kiểm tra post có tồn tại không
        Optional<Post> post = getPostById(comment.getPostId());
        if (!post.isPresent()) {
            System.out.println("Không thể thêm bình luận: không tìm thấy bài viết với ID=" + comment.getPostId());
            return false;
        }
        commentDao.insert(comment);
        
        return true;
    }
    
    public List<Comment> getCommentsByPostId(int postId) {
        return commentDao.findByPostId(postId);
    }
    
    public boolean deleteComment(int id) {
        return commentDao.deleteById(id);
    }
    
}
