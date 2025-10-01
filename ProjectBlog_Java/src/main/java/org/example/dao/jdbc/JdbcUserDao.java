package org.example.dao.jdbc;

import org.example.config.Database;
import org.example.dao.UserDao;
import org.example.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

public class JdbcUserDao implements UserDao {
    
    @Override
    public Optional<User> findByUsername(String username) {
        User ketQua = null;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = Database.getConnection();
            
            // Bước 2: tạo ra đối tượng statement
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, username);
            
            // Bước 3: thực thi câu lệnh SQL
            System.out.println(sql);
            ResultSet rs = st.executeQuery();
            
            // Bước 4: xử lý kết quả
            while (rs.next()) {
                int id = rs.getInt("id");
                String username_db = rs.getString("username");
                String passwordHash = rs.getString("password_hash");
                String displayName = rs.getString("display_name");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                
                ketQua = new User(id, username_db, passwordHash, displayName, createdAt);
            }
            
            // Bước 5: đóng kết nối
            Database.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ketQua != null ? Optional.of(ketQua) : Optional.empty();
    }
    
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        User ketQua = null;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = Database.getConnection();
            
            // Bước 2: tạo ra đối tượng statement
            String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, username);
            st.setString(2, password);
            
            // Bước 3: thực thi câu lệnh SQL
            System.out.println(sql);
            ResultSet rs = st.executeQuery();
            
            // Bước 4: xử lý kết quả
            while (rs.next()) {
                int id = rs.getInt("id");
                String username_db = rs.getString("username");
                String passwordHash = rs.getString("password_hash");
                String displayName = rs.getString("display_name");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                
                ketQua = new User(id, username_db, passwordHash, displayName, createdAt);
            }
            
            // Bước 5: đóng kết nối
            Database.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ketQua != null ? Optional.of(ketQua) : Optional.empty();
    }
    
    @Override
    public User insert(User user) {
        int ketQua = 0;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = Database.getConnection();
            
            // Bước 2: tạo ra đối tượng statement
            String sql = "INSERT INTO users(username, password_hash, display_name) VALUES(?, ?, ?)";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, user.getUsername());
            st.setString(2, user.getPasswordHash());
            st.setString(3, user.getDisplayName());
            
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
        
        return user;
    }
}


