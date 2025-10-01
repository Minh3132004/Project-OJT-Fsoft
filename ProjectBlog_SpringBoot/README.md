# Blog Platform - Spring Boot Application

A comprehensive personal blog platform built with Spring Boot, featuring user authentication, post management, and comment functionality.

## Features

### Public Features
- **Blog Homepage**: View all published blog posts
- **Post Details**: Read individual posts with full content
- **Comment System**: Leave comments on blog posts
- **Search Functionality**: Search posts by title and content
- **Responsive Design**: Works on desktop and mobile devices

### Admin Features
- **Authentication**: Secure login for administrators
- **Dashboard**: Overview of blog statistics and recent posts
- **Post Management**: Create, edit, and delete blog posts
- **Comment Management**: View and delete inappropriate comments
- **User-Friendly Interface**: Intuitive admin panel

## Technology Stack

- **Backend**: Java 21, Spring Boot 3.5.6
- **Database**: H2 (development), MySQL/PostgreSQL (production)
- **Frontend**: Thymeleaf, Bootstrap 5, Font Awesome
- **Security**: Spring Security with BCrypt password encoding
- **Validation**: Bean Validation (Jakarta Validation)
- **Build Tool**: Maven

## Project Structure

```
src/main/java/
├── com/example/Blog_SpringBoot/
│   └── BlogSpringBootApplication.java
├── config/
│   ├── SecurityConfig.java
│   ├── WebConfig.java
│   └── DataInitializer.java
├── controller/
│   ├── BlogController.java
│   ├── AdminController.java
│   ├── PostController.java
│   └── CommentController.java
├── service/
│   ├── UserService.java
│   ├── PostService.java
│   └── CommentService.java
├── repository/
│   ├── UserRepository.java
│   ├── PostRepository.java
│   └── CommentRepository.java
├── model/
│   ├── User.java
│   ├── Post.java
│   └── Comment.java
└── exception/
    └── GlobalExceptionHandler.java
```

## Getting Started

### Prerequisites
- Java 21 or higher
- Maven 3.6 or higher

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Blog_SpringBoot
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - Blog Homepage: http://localhost:8080
   - Admin Login: http://localhost:8080/login
   - H2 Database Console: http://localhost:8080/h2-console

### Default Credentials
- **Username**: admin
- **Password**: admin123

## API Endpoints

### Public API
- `GET /api/posts` - Get all posts
- `GET /api/posts/{id}` - Get post by ID
- `GET /api/posts/search?keyword={keyword}` - Search posts
- `GET /api/comments/post/{postId}` - Get comments for a post

### Admin API (Requires Authentication)
- `POST /api/posts` - Create new post
- `PUT /api/posts/{id}` - Update post
- `DELETE /api/posts/{id}` - Delete post
- `DELETE /api/comments/{id}` - Delete comment

## Database Schema

### Users Table
- `id` (Primary Key)
- `user_name` (Unique)
- `password_hash`
- `display_name`
- `created_at`

### Posts Table
- `id` (Primary Key)
- `title`
- `content`
- `author_id` (Foreign Key)
- `created_at`
- `updated_at`

### Comments Table
- `id` (Primary Key)
- `post_id` (Foreign Key)
- `author_name`
- `content`
- `created_at`

## Security Features

- **Authentication**: Form-based login with Spring Security
- **Authorization**: Role-based access control (ADMIN role)
- **Password Encryption**: BCrypt password hashing
- **CSRF Protection**: Enabled for web forms
- **Input Validation**: Bean validation on all user inputs

## Development Features

- **Hot Reload**: Spring Boot DevTools for development
- **Database Console**: H2 console for database inspection
- **Error Handling**: Global exception handler with user-friendly error pages
- **Validation**: Comprehensive input validation with error messages
- **Sample Data**: Automatic initialization with sample posts and comments

## Testing

The application includes sample data that is automatically loaded on startup:
- 1 admin user (admin/admin123)
- 3 sample blog posts
- 4 sample comments

## Production Deployment

For production deployment:

1. **Database Configuration**: Update `application.properties` to use MySQL or PostgreSQL
2. **Security**: Change default admin credentials
3. **Environment Variables**: Use environment variables for sensitive configuration
4. **HTTPS**: Configure SSL/TLS for secure communication

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For support or questions, please contact the development team or create an issue in the repository.
