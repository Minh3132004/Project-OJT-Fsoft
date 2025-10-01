package org.example.service;

import org.example.model.User;
import org.example.model.Post;
import org.example.model.Comment;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class BlogManager {
    private List<User> users = new ArrayList<>();
    private List<Post> posts = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();
    
    // File để lưu dữ liệu bằng Serialization
    private static final String DATA_FILE = "blog_data.dat";
    
    // ========== USER MANAGEMENT ==========
    
    public void addUser(User user) {
        users.add(user);
    }
    
    public Optional<User> findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
    
    public Optional<User> authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && 
                user.getPasswordHash().equals(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
    
    // ========== POST MANAGEMENT ==========
    
    public void addPost(Post post) {
        posts.add(post);
    }

    public List<Post> getAllPosts() {
        List<Post> copy = new ArrayList<>();
        for (Post post : posts) {
            copy.add(post);
        }
        return copy;
    }
    
    public Optional<Post> getPostById(int id) {
        for (Post post : posts) {
            if (post.getId() == id) {
                return Optional.of(post);
            }
        }
        return Optional.empty();
    }
    
    public boolean updatePost(Post updatedPost) {
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getId() == updatedPost.getId()) {
                posts.set(i, updatedPost);
                return true;
            }
        }
        return false;
    }
    
    public boolean deletePost(int postId) {
        // Xóa post
        boolean postDeleted = false;
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getId() == postId) {
                posts.remove(i);
                postDeleted = true;
                break;
            }
        }
        
        // Xóa tất cả comments của post đó
        for (int i = comments.size() - 1; i >= 0; i--) {
            if (comments.get(i).getPostId() == postId) {
                comments.remove(i);
            }
        }
        
        return postDeleted;
    }
    
    // ========== COMMENT MANAGEMENT ==========
    
    public boolean addComment(Comment comment) {

        Optional<Post> post = getPostById(comment.getPostId());
        if (!post.isPresent()) {
            System.out.println("Không thể thêm bình luận: không tìm thấy bài viết với ID=" + comment.getPostId());
            return false;
        }
        comments.add(comment);
        return true;
    }

    // Bảo vệ bằng synchronized cho bài toán đa luồng (Task 4)
    public synchronized boolean addCommentSynchronized(Comment comment) {
        Optional<Post> post = getPostById(comment.getPostId());
        if (!post.isPresent()) {
            System.out.println("Không thể thêm bình luận: không tìm thấy bài viết với ID=" + comment.getPostId());
            return false;
        }
        comments.add(comment);
        return true;
    }
    
    public List<Comment> getCommentsByPostId(int postId) {
        List<Comment> postComments = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getPostId() == postId) {
                postComments.add(comment);
            }
        }

        return postComments;
    }
    
    public boolean deleteComment(int commentId) {
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).getId() == commentId) {
                comments.remove(i);
                return true;
            }
        }
        return false;
    }
    
    // ========== DATA PERSISTENCE ==========
    
    public void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            BlogData data = new BlogData(users, posts, comments);
            out.writeObject(data);
            System.out.println("Đã lưu dữ liệu vào file: " + DATA_FILE);
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu file: " + e.getMessage());
        }
    }
    
    public void loadFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            BlogData data = (BlogData) in.readObject();
            this.users = data.getUsers();
            this.posts = data.getPosts();
            this.comments = data.getComments();
            
            // Đồng bộ comments vào posts
            for (Post post : posts) {
                post.getComments().clear(); // Xóa comments cũ
                List<Comment> comments = new ArrayList<>();
                for (Comment comment : this.comments) {
                    if (comment.getPostId() == post.getId()) {
                        comments.add(comment);
                    }
                }
                post.setComments(comments);
            }
            
            System.out.println("Đã tải dữ liệu từ file: " + DATA_FILE);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Lỗi khi tải file: " + e.getMessage());
            System.out.println("Khởi tạo dữ liệu mới...");
        }
    }
    

    // ========== UTILITY METHODS ==========
    
    public int getNextPostId() {
        int maxId = 0;
        for (Post post : posts) {
            if (post.getId() > maxId) {
                maxId = post.getId();
            }
        }
        return maxId + 1;
    }
    
    public int getNextCommentId() {
        int maxId = 0;
        for (Comment comment : comments) {
            if (comment.getId() > maxId) {
                maxId = comment.getId();
            }
        }
        return maxId + 1;
    }
    
    public int getNextUserId() {
        int maxId = 0;
        for (User user : users) {
            if (user.getId() > maxId) {
                maxId = user.getId();
            }
        }
        return maxId + 1;
    }
    
    // ========== INNER CLASS FOR SERIALIZATION ==========
    
    private static class BlogData implements Serializable {
        private static final long serialVersionUID = 1L;
        private List<User> users;
        private List<Post> posts;
        private List<Comment> comments;
        
        public BlogData(List<User> users, List<Post> posts, List<Comment> comments) {
            this.users = new ArrayList<>(users);
            this.posts = new ArrayList<>(posts);
            this.comments = new ArrayList<>(comments);
        }
        
        public List<User> getUsers() { return users; }
        public List<Post> getPosts() { return posts; }
        public List<Comment> getComments() { return comments; }
    }

    // ========== MENU ==========
    
    public static void main(String[] args) {
        BlogManager app = new BlogManager();
        app.loadFromFile();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        User loggedIn = null;
        while (running) {
            System.out.println("\n===== BLOG MANAGER MENU =====");
            if (loggedIn != null) {
                System.out.println("[Đang đăng nhập: " + loggedIn.getDisplayName() + "]");
            } else {
                System.out.println("[Chưa đăng nhập]");
            }
            System.out.println("1. Danh sách bài viết");
            System.out.println("2. Xem chi tiết bài viết + bình luận");
            System.out.println("3. Thêm bài viết (admin)");
            System.out.println("4. Xóa bài viết (admin)");
            System.out.println("5. Thêm bình luận cho bài viết");
            System.out.println("6. Xóa bình luận");
            System.out.println("7. Lưu dữ liệu ra file");
            System.out.println("8. Đăng ký tài khoản (owner)");
            System.out.println("9. Đăng nhập");
            System.out.println("10.Đăng xuất");
            System.out.println("11.Mô phỏng đa luồng thêm bình luận");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine();
            if (choice == null) {
                choice = "";
            }

            if ("1".equals(choice)) {
                List<Post> all = app.getAllPosts();
                if (all.isEmpty()) {
                    System.out.println("(Trống)");
                } else {
                    for (Post p : all) {
                        System.out.println("ID=" + p.getId() + " | " + p.getTitle());
                    }
                }
            } else if ("2".equals(choice)) {
                System.out.print("Nhập postId: ");
                String s = scanner.nextLine();
                int postId = 0;
                try {
                    postId = Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    System.out.println("Giá trị không hợp lệ");
                    continue;
                }
                Optional<Post> op = app.getPostById(postId);
                if (!op.isPresent()) {
                    System.out.println("Không tìm thấy bài viết");
                } else {
                    Post p = op.get();
                    System.out.println("\n--- BÀI VIẾT ---");
                    System.out.println("ID: " + p.getId());
                    System.out.println("Tiêu đề: " + p.getTitle());
                    System.out.println("Nội dung:\n" + p.getContent());
                    System.out.println("\n--- BÌNH LUẬN ---");
                    List<Comment> cs = app.getCommentsByPostId(postId);
                    if (cs.isEmpty()) {
                        System.out.println("(Chưa có bình luận)");
                    } else {
                        for (Comment c : cs) {
                            System.out.println("#" + c.getId() + " | " + c.getAuthorName() + ": " + c.getContent());
                        }
                    }
                }
            } else if ("3".equals(choice)) {
                if (loggedIn == null) {
                    System.out.println("Vui lòng đăng nhập (admin) trước khi thêm bài viết.");
                    continue;
                }
                System.out.print("Tiêu đề: ");
                String title = scanner.nextLine();
                System.out.print("Nội dung: ");
                String content = scanner.nextLine();
                Post post = new Post();
                post.setId(app.getNextPostId());
                post.setTitle(title);
                post.setContent(content);
                post.setCreatedAt(LocalDateTime.now());
                app.addPost(post);
                System.out.println("Đã thêm bài viết với ID=" + post.getId());
            } else if ("4".equals(choice)) {
                if (loggedIn == null) {
                    System.out.println("Vui lòng đăng nhập (admin) trước khi xóa bài viết.");
                    continue;
                }
                System.out.print("Nhập postId cần xóa: ");
                String s = scanner.nextLine();
                int postId = 0;
                try {
                    postId = Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    System.out.println("Giá trị không hợp lệ");
                    continue;
                }
                boolean ok = app.deletePost(postId);
                if (ok) {
                    System.out.println("Đã xóa bài viết và các bình luận liên quan.");
                } else {
                    System.out.println("Không tìm thấy bài viết.");
                }
            } else if ("5".equals(choice)) {
                System.out.print("Nhập postId: ");
                String sPost = scanner.nextLine();
                int postId = 0;
                try {
                    postId = Integer.parseInt(sPost);
                } catch (NumberFormatException e) {
                    System.out.println("Giá trị không hợp lệ");
                    continue;
                }
                System.out.print("Tên người bình luận: ");
                String author = scanner.nextLine();
                System.out.print("Nội dung bình luận: ");
                String content = scanner.nextLine();
                Comment cmt = new Comment();
                cmt.setId(app.getNextCommentId());
                cmt.setPostId(postId);
                cmt.setAuthorName(author);
                cmt.setContent(content);
                cmt.setCreatedAt(LocalDateTime.now());
                boolean ok = app.addComment(cmt);
                if (ok) {
                    System.out.println("Đã thêm bình luận #" + cmt.getId());
                }
            } else if ("6".equals(choice)) {
                if (loggedIn == null) {
                    System.out.println("Vui lòng đăng nhập (admin) trước khi xóa bình luận.");
                    continue;
                }
                System.out.print("Nhập commentId cần xóa: ");
                String s = scanner.nextLine();
                int commentId = 0;
                try {
                    commentId = Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    System.out.println("Giá trị không hợp lệ");
                    continue;
                }
                boolean ok = app.deleteComment(commentId);
                if (ok) {
                    System.out.println("Đã xóa bình luận.");
                } else {
                    System.out.println("Không tìm thấy bình luận.");
                }
            } else if ("7".equals(choice)) {
                app.saveToFile();
            } else if ("8".equals(choice)) {
                // Đăng ký owner (đơn giản: thêm user mới)
                System.out.print("Username: ");
                String username = scanner.nextLine();
                if (username == null) username = "";
                Optional<User> existed = app.findUserByUsername(username);
                if (existed.isPresent()) {
                    System.out.println("Username đã tồn tại.");
                    continue;
                }
                System.out.print("Mật khẩu: ");
                String password = scanner.nextLine();
                System.out.print("Tên hiển thị: ");
                String display = scanner.nextLine();
                User u = new User();
                u.setId(app.getNextUserId());
                u.setUsername(username);
                u.setPasswordHash(password);
                u.setDisplayName(display);
                u.setCreatedAt(LocalDateTime.now());
                app.addUser(u);
                System.out.println("Đăng ký thành công (ID=" + u.getId() + ")");
            } else if ("9".equals(choice)) {
                System.out.print("Username: ");
                String username = scanner.nextLine();
                System.out.print("Mật khẩu: ");
                String password = scanner.nextLine();
                Optional<User> u = app.authenticateUser(username, password);
                if (u.isPresent()) {
                    loggedIn = u.get();
                    System.out.println("Đăng nhập thành công.");
                } else {
                    System.out.println("Sai thông tin đăng nhập.");
                }
            } else if ("10".equals(choice)) {
                loggedIn = null;
                System.out.println("Đã đăng xuất.");
            } else if ("11".equals(choice)) {
                // Task 4: Multithreading simulation
                System.out.print("Nhập postId: ");
                String sPost = scanner.nextLine();
                int postId = 0;
                try {
                    postId = Integer.parseInt(sPost);
                } catch (NumberFormatException e) {
                    System.out.println("Giá trị không hợp lệ");
                    continue;
                }
                Optional<Post> op = app.getPostById(postId);
                if (!op.isPresent()) {
                    System.out.println("Không tìm thấy bài viết");
                    continue;
                }
                System.out.print("Số luồng: ");
                String sThreads = scanner.nextLine();
                int numThreads = 0;
                try {
                    numThreads = Integer.parseInt(sThreads);
                } catch (NumberFormatException e) {
                    System.out.println("Giá trị không hợp lệ");
                    continue;
                }
                System.out.print("Số bình luận mỗi luồng: ");
                String sEach = scanner.nextLine();
                int commentsPerThread = 0;
                try {
                    commentsPerThread = Integer.parseInt(sEach);
                } catch (NumberFormatException e) {
                    System.out.println("Giá trị không hợp lệ");
                    continue;
                }
                System.out.print("Dùng synchronized? (y/n): ");
                String useSyncStr = scanner.nextLine();
                boolean useSync = "y".equalsIgnoreCase(useSyncStr);

                final int postIdFinal = postId;
                final int commentsPerThreadFinal = commentsPerThread;
                final boolean useSyncFinal = useSync;
                Thread[] arr = new Thread[numThreads];
                for (int i = 0; i < numThreads; i++) {
                    final int threadIndex = i;
                    Runnable r = new Runnable() {
                        public void run() {
                            for (int k = 0; k < commentsPerThreadFinal; k++) {
                                Comment cmt = new Comment();
                                synchronized (app) {
                                    // Bảo đảm ID duy nhất khi chạy song song trong in-memory
                                    cmt.setId(app.getNextCommentId());
                                }
                                cmt.setPostId(postIdFinal);
                                cmt.setAuthorName("User-" + threadIndex);
                                cmt.setContent("Cmt-" + k + " từ luồng " + threadIndex);
                                cmt.setCreatedAt(LocalDateTime.now());
                                if (useSyncFinal) {
                                    app.addCommentSynchronized(cmt);
                                } else {
                                    app.addComment(cmt);
                                }
                                try {
                                    Thread.sleep(5);
                                } catch (InterruptedException e) {
                                    // ignore
                                }
                            }
                        }
                    };
                    arr[i] = new Thread(r);
                }
                for (int i = 0; i < numThreads; i++) {
                    arr[i].start();
                }
                for (int i = 0; i < numThreads; i++) {
                    try {
                        arr[i].join();
                    } catch (InterruptedException e) {
                        // ignore
                    }
                }
                List<Comment> cs = app.getCommentsByPostId(postId);
                System.out.println("Hoàn tất. Tổng bình luận của post " + postId + ": " + cs.size());
            } else if ("0".equals(choice)) {
                running = false;
            } else {
                System.out.println("Lựa chọn không hợp lệ");
            }
        }
        scanner.close();
        System.out.println("Tạm biệt!");
    }
}
