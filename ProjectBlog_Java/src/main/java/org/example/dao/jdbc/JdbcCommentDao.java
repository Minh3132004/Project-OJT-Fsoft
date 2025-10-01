package org.example.dao.jdbc;

import org.example.dao.CommentDao;
import org.example.config.Database;
import org.example.model.Comment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcCommentDao implements CommentDao {
    
    @Override
    public Comment insert(Comment comment) {
        int ketQua = 0;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = Database.getConnection();
            
            // Bước 2: tạo ra đối tượng statement
            String sql = "INSERT INTO comments(post_id, author_name, content) VALUES(?, ?, ?)";
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, comment.getPostId());
            st.setString(2, comment.getAuthorName());
            st.setString(3, comment.getContent());
            
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
        
        return comment;
    }
    
    @Override
    public boolean deleteById(int id) {
        int ketQua = 0;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = Database.getConnection();
            
            // Bước 2: tạo ra đối tượng statement
            String sql = "DELETE FROM comments WHERE id = ?";
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
    public List<Comment> findByPostId(int postId) {
        List<Comment> ketQua = new ArrayList<>();
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = Database.getConnection();
            
            // Bước 2: tạo ra đối tượng statement
            String sql = "SELECT * FROM comments WHERE post_id = ? ORDER BY created_at ASC";
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, postId);
            
            // Bước 3: thực thi câu lệnh SQL
            System.out.println(sql);
            ResultSet rs = st.executeQuery();
            
            // Bước 4: xử lý kết quả
            while (rs.next()) {
                int id = rs.getInt("id");
                int postId_db = rs.getInt("post_id");
                String authorName = rs.getString("author_name");
                String content = rs.getString("content");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                
                Comment comment = new Comment(id, postId_db, authorName, content, createdAt);
                ketQua.add(comment);
            }
            
            // Bước 5: đóng kết nối
            Database.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ketQua;
    }
}


