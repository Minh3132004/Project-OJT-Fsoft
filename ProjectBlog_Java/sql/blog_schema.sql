
Create Database BlogDB;

Use BlogDB;

-- Chủ blog (owner)
CREATE TABLE users (
  id INT IDENTITY(1,1) PRIMARY KEY,
  username NVARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(60) NOT NULL,
  display_name NVARCHAR(150) NOT NULL,
  created_at DATETIME2(3) NOT NULL DEFAULT SYSUTCDATETIME()
);

-- Bài viết
CREATE TABLE posts (
  id INT IDENTITY(1,1) PRIMARY KEY,
  title NVARCHAR(200) NOT NULL,
  content NVARCHAR(MAX) NOT NULL,
  author_id INT NOT NULL
    CONSTRAINT FK_posts_user REFERENCES users(id),
  created_at DATETIME2(3) NOT NULL DEFAULT SYSUTCDATETIME(),
  updated_at DATETIME2(3) NULL
);

-- Bình luận của khách (ẩn danh: lưu tên nhập vào)
CREATE TABLE comments (
  id INT IDENTITY(1,1) PRIMARY KEY,
  post_id INT NOT NULL
    CONSTRAINT FK_comments_post REFERENCES posts(id) ON DELETE CASCADE,
  author_name NVARCHAR(150) NOT NULL,
  content NVARCHAR(1000) NOT NULL,
  created_at DATETIME2(3) NOT NULL DEFAULT SYSUTCDATETIME()
);

