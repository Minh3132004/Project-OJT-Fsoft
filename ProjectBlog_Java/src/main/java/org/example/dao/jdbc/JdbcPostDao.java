package org.example.dao.jdbc;

import org.example.config.Database;
import org.example.dao.PostDao;
import org.example.model.Post;
import org.example.model.Comment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcPostDao implements PostDao {
    
    @Override
    public Post insert(Post post) {
        int ketQua = 0;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = Database.getConnection();
            
            // Bước 2: tạo ra đối tượng statement
            String sql = "INSERT INTO posts(title, content, author_id) VALUES(?, ?, ?)";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, post.getTitle());
            st.setString(2, post.getContent());
            st.setInt(3, post.getAuthorId());
            
            // Bước 3: thực thi câu lệnh SQL
            ketQua = st.executeUpdate();
            
            // Bước 4:
            System.out.println("Bạn đã thực thi: " + sql);
            System.out.println("Có " + ketQua + " dòng bị thay đổi!");
            
            // Bước 5:
            Database.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return post;
    }
    
    @Override
    public boolean update(Post post) {
        int ketQua = 0;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = Database.getConnection();
            
            // Bước 2: tạo ra đối tượng statement
            String sql = "UPDATE posts SET title = ?, content = ?, updated_at = SYSUTCDATETIME() WHERE id = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, post.getTitle());
            st.setString(2, post.getContent());
            st.setInt(3, post.getId());
            
            // Bước 3: thực thi câu lệnh SQL
            System.out.println(sql);
            ketQua = st.executeUpdate();
            
            // Bước 4:
            System.out.println("Bạn đã thực thi: " + sql);
            System.out.println("Có " + ketQua + " dòng bị thay đổi!");
            
            // Bước 5:
            Database.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ketQua > 0;
    }
    
    @Override
    public boolean deleteById(int id) {
        int ketQua = 0;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = Database.getConnection();
            
            // Bước 2: tạo ra đối tượng statement
            String sql = "DELETE FROM posts WHERE id = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            
            // Bước 3: thực thi câu lệnh SQL
            System.out.println(sql);
            ketQua = st.executeUpdate();
            
            // Bước 4:
            System.out.println("Bạn đã thực thi: " + sql);
            System.out.println("Có " + ketQua + " dòng bị thay đổi!");
            
            // Bước 5:
            Database.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ketQua > 0;
    }
    
    @Override
    public Optional<Post> findById(int id) {
        Post ketQua = null;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = Database.getConnection();
            
            // Bước 2: tạo ra đối tượng statement
            String sql = "SELECT * FROM posts WHERE id = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            
            // Bước 3: thực thi câu lệnh SQL
            System.out.println(sql);
            ResultSet rs = st.executeQuery();
            
            // Bước 4: xử lý kết quả
            while (rs.next()) {
                int postId = rs.getInt("id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                int authorId = rs.getInt("author_id");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime updatedAt = rs.getTimestamp("updated_at") != null ? 
                    rs.getTimestamp("updated_at").toLocalDateTime() : null;
                
                ketQua = new Post(postId, title, content, authorId, createdAt, updatedAt);
            }
            
            // Bước 5: đóng kết nối
            Database.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ketQua != null ? Optional.of(ketQua) : Optional.empty();
    }

    @Override
    public List<Post> findAll() {
        List<Post> ketQua = new ArrayList<>();
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = Database.getConnection();
            
            // Bước 2: tạo ra đối tượng statement
            String sql = "SELECT * FROM posts ORDER BY created_at DESC";
            PreparedStatement st = con.prepareStatement(sql);
            
            // Bước 3: thực thi câu lệnh SQL
            System.out.println(sql);
            ResultSet rs = st.executeQuery();
            
            // Bước 4: xử lý kết quả
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                int authorId = rs.getInt("author_id");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime updatedAt = rs.getTimestamp("updated_at") != null ? 
                    rs.getTimestamp("updated_at").toLocalDateTime() : null;
                
                Post post = new Post(id, title, content, authorId, createdAt, updatedAt);
                ketQua.add(post);
            }
            
            // Bước 5: đóng kết nối
            Database.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ketQua;
    }

    @Override
    public Optional<Post> findByIdWithComments(int id) {
        Post ketQua = null;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = Database.getConnection();
            
            // Bước 2: tạo ra đối tượng statement với JOIN
            String sql = "SELECT p.id, p.title, p.content, p.author_id, p.created_at, p.updated_at, " +
                        "c.id as comment_id, c.author_name, c.content as comment_content, c.created_at as comment_created_at " +
                        "FROM posts p " +
                        "LEFT JOIN comments c ON p.id = c.post_id " +
                        "WHERE p.id = ? " +
                        "ORDER BY c.created_at ASC";
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            
            // Bước 3: thực thi câu lệnh SQL
            System.out.println(sql);
            ResultSet rs = st.executeQuery();
            
            // Bước 4: xử lý kết quả
            List<Comment> comments = new ArrayList<>();
            while (rs.next()) {
                // Tạo Post chỉ một lần
                if (ketQua == null) {
                    int postId = rs.getInt("id");
                    String title = rs.getString("title");
                    String content = rs.getString("content");
                    int authorId = rs.getInt("author_id");
                    LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                    LocalDateTime updatedAt = rs.getTimestamp("updated_at") != null ? 
                        rs.getTimestamp("updated_at").toLocalDateTime() : null;
                    
                    ketQua = new Post(postId, title, content, authorId, createdAt, updatedAt);
                }
                
                // Thêm Comment nếu có
                int commentId = rs.getInt("comment_id");
                if (!rs.wasNull()) { // Kiểm tra comment_id không null
                    String authorName = rs.getString("author_name");
                    String commentContent = rs.getString("comment_content");
                    LocalDateTime commentCreatedAt = rs.getTimestamp("comment_created_at").toLocalDateTime();
                    
                    Comment comment = new Comment(commentId, id, authorName, commentContent, commentCreatedAt);
                    comments.add(comment);
                }
            }
            
            // Gán comments vào post
            if (ketQua != null) {
                ketQua.setComments(comments);
            }
            
            // Bước 5: đóng kết nối
            Database.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ketQua != null ? Optional.of(ketQua) : Optional.empty();
    }

    @Override
    public List<Post> searchByKeyword(String keyword) {
        List<Post> ketQua = new ArrayList<>();
        try {
            Connection con = Database.getConnection();
            String sql = "SELECT * FROM posts WHERE title LIKE ? OR content LIKE ? ORDER BY created_at DESC";
            PreparedStatement st = con.prepareStatement(sql);
            String pattern = "%" + (keyword == null ? "" : keyword.trim()) + "%";
            st.setString(1, pattern);
            st.setString(2, pattern);
            System.out.println(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                int authorId = rs.getInt("author_id");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime updatedAt = rs.getTimestamp("updated_at") != null ?
                    rs.getTimestamp("updated_at").toLocalDateTime() : null;
                Post post = new Post(id, title, content, authorId, createdAt, updatedAt);
                ketQua.add(post);
            }
            Database.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ketQua;
    }
}


