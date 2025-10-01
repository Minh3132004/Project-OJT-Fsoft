# Blog Management System

Ứng dụng Java console quản lý blog cá nhân với JDBC và Microsoft SQL Server.

## Yêu cầu hệ thống

- Java 11+
- Microsoft SQL Server
- Maven 3.6+

## Cài đặt

### 1. Tạo Database
Chạy file `sql/blog_schema.sql` trong SQL Server:
```sql
Create Database BlogDB;
Use BlogDB;
-- Chạy các CREATE TABLE trong file blog_schema.sql
```

### 2. Cấu hình kết nối
Sửa file `Database.java` hoặc set environment variables:
```bash
DB_URL=jdbc:sqlserver://localhost:1433;databaseName=BlogDB;encrypt=true;trustServerCertificate=true
DB_USER=sa
DB_PASS=your_password
```

### 3. Chạy ứng dụng
Run class Main .

## Sử dụng

### Menu chính:
1. **Danh sách bài viết** - Xem tất cả bài viết
2. **Xem chi tiết bài viết + bình luận** - Xem bài viết và bình luận
3. **Thêm bài viết (admin)** - Tạo bài viết mới (cần đăng nhập)
4. **Xóa bài viết (admin)** - Xóa bài viết (cần đăng nhập)
5. **Thêm bình luận** - Thêm bình luận (không cần đăng nhập)
6. **Xóa bình luận (admin)** - Xóa bình luận không phù hợp (cần đăng nhập)
7. **Đăng ký (owner)** - Đăng ký tài khoản admin
8. **Đăng nhập** - Đăng nhập admin
9. **Đăng xuất** - Đăng xuất
0. **Thoát** - Thoát ứng dụng

### Quyền hạn:
- **Admin**: Tạo/sửa/xóa bài viết, xóa bình luận
- **Khách**: Xem bài viết, thêm bình luận

## Cấu trúc dự án

```
src/main/java/org/example/
├── Main.java                 # Chương trình chính
├── config/Database.java      # Kết nối database
├── dao/                      # Interface DAO
├── dao/jdbc/                 # Implementation JDBC
├── model/                    # Entity classes
└── service/                  # Business logic
```

