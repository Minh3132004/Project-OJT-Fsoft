package org.example;

import org.example.model.Comment;
import org.example.model.Post;
import org.example.model.User;
import org.example.service.JdbcBlogService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        JdbcBlogService app = new JdbcBlogService();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        User loggedIn = null;
        while (running) {
            System.out.println("\n===== BLOG APP (MAIN MENU) =====");
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
            System.out.println("6. Xóa bình luận (admin)");
            System.out.println("7. Đăng ký (owner)");
            System.out.println("8. Đăng nhập");
            System.out.println("9. Đăng xuất");
            System.out.println("10. Tìm kiếm bài viết theo tiêu đề/nội dung");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            String choice = scanner.nextLine();
            if (choice == null) choice = "";

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
                try { postId = Integer.parseInt(s); } catch (NumberFormatException e) { System.out.println("Không hợp lệ"); continue; }
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
                    List<Comment> cs = p.getComments();
                    if (cs.isEmpty()) System.out.println("(Chưa có bình luận)");
                    else {
                        for (Comment c : cs) {
                            System.out.println("#" + c.getId() + " | " + c.getAuthorName() + ": " + c.getContent());
                        }
                    }
                }
            } else if ("3".equals(choice)) {
                if (loggedIn == null) { System.out.println("Vui lòng đăng nhập trước."); continue; }
                System.out.print("Tiêu đề: ");
                String title = scanner.nextLine();
                System.out.print("Nội dung: ");
                String content = scanner.nextLine();
                Post post = new Post();
                post.setTitle(title);
                post.setContent(content);
                post.setAuthorId(loggedIn.getId());
                post.setCreatedAt(LocalDateTime.now());
                app.addPost(post);
                System.out.println("Đã thêm bài viết");
            } else if ("4".equals(choice)) {
                if (loggedIn == null) { System.out.println("Vui lòng đăng nhập trước."); continue; }
                System.out.print("Nhập postId cần xóa: ");
                String s = scanner.nextLine();
                int postId = 0;
                try { postId = Integer.parseInt(s); } catch (NumberFormatException e) { System.out.println("Không hợp lệ"); continue; }
                boolean ok = app.deletePost(postId);
                System.out.println(ok ? "Đã xóa bài viết." : "Không tìm thấy bài viết.");
            } else if ("5".equals(choice)) {
                System.out.print("Nhập postId: ");
                String sPost = scanner.nextLine();
                int postId = 0;
                try { postId = Integer.parseInt(sPost); } catch (NumberFormatException e) { System.out.println("Không hợp lệ"); continue; }
                System.out.print("Tên người bình luận: ");
                String author = scanner.nextLine();
                System.out.print("Nội dung bình luận: ");
                String content = scanner.nextLine();
                Comment cmt = new Comment();
                cmt.setPostId(postId);
                cmt.setAuthorName(author);
                cmt.setContent(content);
                cmt.setCreatedAt(LocalDateTime.now());
                boolean ok = app.addComment(cmt);
                if (ok) System.out.println("Đã thêm bình luận");
            } else if ("6".equals(choice)) {
                if (loggedIn == null) { System.out.println("Vui lòng đăng nhập trước."); continue; }
                System.out.print("Nhập commentId cần xóa: ");
                String s = scanner.nextLine();
                int commentId = 0;
                try { commentId = Integer.parseInt(s); } catch (NumberFormatException e) { System.out.println("Không hợp lệ"); continue; }
                boolean ok = app.deleteComment(commentId);
                System.out.println(ok ? "Đã xóa bình luận." : "Không tìm thấy bình luận.");
            } else if ("7".equals(choice)) {
                System.out.print("Username: ");
                String username = scanner.nextLine();
                if (username == null) username = "";
                Optional<User> existed = app.findUserByUsername(username);
                if (existed.isPresent()) { System.out.println("Username đã tồn tại."); continue; }
                System.out.print("Mật khẩu: ");
                String password = scanner.nextLine();
                System.out.print("Tên hiển thị: ");
                String display = scanner.nextLine();
                User u = new User();
                u.setUsername(username);
                u.setPasswordHash(password);
                u.setDisplayName(display);
                u.setCreatedAt(LocalDateTime.now());
                app.addUser(u);
                System.out.println("Đăng ký thành công");
            } else if ("8".equals(choice)) {
                System.out.print("Username: ");
                String username = scanner.nextLine();
                System.out.print("Mật khẩu: ");
                String password = scanner.nextLine();
                Optional<User> u = app.authenticateUser(username, password);
                if (u.isPresent()) { loggedIn = u.get(); System.out.println("Đăng nhập thành công."); }
                else { System.out.println("Sai thông tin đăng nhập."); }
            } else if ("9".equals(choice)) {
                loggedIn = null;
                System.out.println("Đã đăng xuất.");
            } else if ("10".equals(choice)) {
                System.out.print("Nhập từ khóa: ");
                String keyword = scanner.nextLine();
                List<Post> results = app.searchPosts(keyword);
                if (results.isEmpty()) {
                    System.out.println("Không tìm thấy bài viết phù hợp.");
                } else {
                    System.out.println("Kết quả tìm kiếm:");
                    for (Post p : results) {
                        System.out.println("ID=" + p.getId() + " | " + p.getTitle());
                    }
                }
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
