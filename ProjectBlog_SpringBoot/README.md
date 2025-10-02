# 📝 Blog SpringBoot Application

Một nền tảng blog cá nhân được xây dựng với Spring Boot, có tính năng xác thực người dùng, quản lý bài viết và hệ thống bình luận.

## ✨ Tính năng chính

### 🌐 Tính năng công khai
- **Trang chủ Blog**: Xem tất cả bài viết blog được xuất bản
- **Chi tiết bài viết**: Đọc từng bài viết với nội dung đầy đủ
- **Hệ thống bình luận**: Để lại bình luận trên các bài viết blog
- **Thiết kế responsive**: Hoạt động trên desktop và mobile

### 🔐 Tính năng quản trị
- **Xác thực**: Đăng nhập an toàn cho quản trị viên
- **Quản lý bài viết**: Tạo, chỉnh sửa và xóa bài viết blog
- **Giao diện thân thiện**: Giao diện quản trị trực quan

## 🛠 Công nghệ sử dụng

- **Backend**: Java 21, Spring Boot 3.5.6
- **Database**: SQL Server (production), H2 (development)
- **Frontend**: Thymeleaf, Bootstrap 5, Font Awesome
- **Security**: Spring Security với mã hóa mật khẩu
- **Validation**: Bean Validation (Jakarta Validation)
- **Monitoring**: Spring Boot Actuator
- **Build Tool**: Maven

## 📁 Cấu trúc dự án

```
src/main/java/com/example/Blog_SpringBoot/
├── BlogSpringBootApplication.java          # Main application class
├── config/
│   └── SecurityConfig.java                 # Spring Security configuration
├── controller/
│   ├── AuthController.java                 # Authentication controller
│   ├── CommentController.java              # Comment management
│   ├── Day1_2_Controller.java             # Day 1-2 exercises
│   ├── HomeController.java                # Homepage controller
│   ├── PostController.java                # Post web controller
│   └── PostRestController.java            # Post REST API
├── service/
│   ├── CustomUserDetailsService.java      # User authentication service
│   ├── PostService.java                   # Post business logic
│   ├── CommentService.java               # Comment business logic
│   └── UserService.java                   # User business logic
├── repository/
│   ├── UserRepository.java                # User data access
│   ├── PostRepository.java               # Post data access
│   └── CommentRepository.java            # Comment data access
└── model/
    ├── User.java                          # User entity
    ├── Post.java                         # Post entity
    └── Comment.java                      # Comment entity

src/main/resources/
├── application.properties                 # Application configuration
└── templates/                            # Thymeleaf templates
    ├── index.html                        # Homepage
    ├── login.html                        # Login page
    ├── register.html                     # Registration page
    ├── day1_2/                          # Day 1-2 templates
    └── posts/                           # Post templates
        ├── detail.html                  # Post detail page
        ├── edit.html                    # Post edit form
        └── new.html                     # New post form
```

## 🚀 Bắt đầu

### Yêu cầu hệ thống
- Java 21 hoặc cao hơn
- Maven 3.6 hoặc cao hơn
- SQL Server (cho production) hoặc H2 (cho development)

### Cài đặt và chạy

1. **Clone repository**
   ```bash
   git clone <repository-url>
   cd ProjectBlog_SpringBoot
   ```

2. **Cấu hình database**
   - Cập nhật `src/main/resources/application.properties`
   - Thay đổi thông tin kết nối SQL Server nếu cần

3. **Build dự án**
   ```bash
   mvn clean install
   ```

4. **Chạy ứng dụng**
   ```bash
   mvn spring-boot:run
   ```

5. **Truy cập ứng dụng**
   - Trang chủ Blog: http://localhost:8080
   - Đăng nhập: http://localhost:8080/login
   - Đăng ký: http://localhost:8080/register
   - Actuator endpoints: http://localhost:8080/actuator

## 🔗 API Endpoints

### Web Endpoints
- `GET /` - Trang chủ blog
- `GET /posts/{id}` - Chi tiết bài viết
- `GET /posts/new` - Form tạo bài viết mới (yêu cầu đăng nhập)
- `GET /posts/{id}/edit` - Form chỉnh sửa bài viết (yêu cầu đăng nhập)
- `POST /posts` - Tạo bài viết mới
- `POST /posts/{id}/edit` - Cập nhật bài viết
- `POST /posts/{id}/delete` - Xóa bài viết
- `GET /comments/{id}` - Form thêm bình luận
- `POST /comments/{id}/add` - Thêm bình luận mới

### REST API
- `GET /api/posts` - Lấy tất cả bài viết
- `GET /api/posts/{id}` - Lấy bài viết theo ID
- `POST /api/posts` - Tạo bài viết mới (yêu cầu xác thực)
- `PUT /api/posts/{id}` - Cập nhật bài viết (yêu cầu xác thực)
- `DELETE /api/posts/{id}` - Xóa bài viết (yêu cầu xác thực)

### Actuator Endpoints
- `GET /actuator` - Danh sách tất cả endpoints
- `GET /actuator/health` - Kiểm tra sức khỏe ứng dụng
- `GET /actuator/info` - Thông tin ứng dụng
- `GET /actuator/metrics` - Application metrics
- `GET /actuator/env` - Environment variables

## 🗄 Cơ sở dữ liệu

### Users Table
- `id` (Primary Key, Identity)
- `username` (Unique, nvarchar(50))
- `password_hash` (nvarchar(100))
- `display_name` (nvarchar(100))
- `created_at` (datetime2)

### Posts Table
- `id` (Primary Key, Identity)
- `title` (nvarchar(200))
- `content` (nvarchar(max))
- `author_id` (Foreign Key → users.id)
- `created_at` (datetime2)
- `updated_at` (datetime2)

### Comments Table
- `id` (Primary Key, Identity)
- `post_id` (Foreign Key → posts.id)
- `author_name` (nvarchar(50))
- `content` (nvarchar(1000))
- `created_at` (datetime2)

## 🔒 Tính năng bảo mật

- **Xác thực**: Form-based login với Spring Security
- **Mã hóa mật khẩu**: Sử dụng {noop} prefix (có thể nâng cấp lên BCrypt)
- **Ủy quyền**: Kiểm soát truy cập dựa trên vai trò
- **CSRF Protection**: Được vô hiệu hóa cho REST API
- **Input Validation**: Validation toàn diện với Bean Validation

## 🎯 Tính năng đặc biệt

- **Responsive Design**: Giao diện đẹp với Bootstrap 5
- **Font Awesome Icons**: Icons hiện đại cho UI
- **Comment System**: Hệ thống bình luận với validation
- **Post Management**: CRUD operations cho bài viết
- **Authentication**: Hệ thống đăng nhập/đăng ký
- **Actuator Monitoring**: Giám sát ứng dụng

## 🔧 Configuration

### Database Configuration
```properties
# SQL Server Configuration
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=BlogDBSpringBoot;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=123
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

# Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.SQLServerDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Actuator Configuration
```properties
# Spring Boot Actuator
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always
```

## 🤝 Đóng góp

1. Fork repository
2. Tạo feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit thay đổi (`git commit -m 'Add some AmazingFeature'`)
4. Push lên branch (`git push origin feature/AmazingFeature`)
5. Tạo Pull Request

## 📄 License

Dự án này được phân phối dưới MIT License. Xem file `LICENSE` để biết thêm chi tiết.

## 📞 Liên hệ

Nếu có câu hỏi hoặc cần hỗ trợ, vui lòng tạo issue trong repository hoặc liên hệ với team phát triển.

---

**Phiên bản**: 0.0.1-SNAPSHOT  
**Java**: 21  
**Spring Boot**: 3.5.6  
**Build Tool**: Maven