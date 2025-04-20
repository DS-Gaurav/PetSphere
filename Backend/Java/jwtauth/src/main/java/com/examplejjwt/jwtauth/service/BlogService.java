package com.examplejjwt.jwtauth.service;

import com.examplejjwt.jwtauth.dto.BlogDetailResponse;
import com.examplejjwt.jwtauth.dto.BlogDto;
import com.examplejjwt.jwtauth.entity.Blog;
import com.examplejjwt.jwtauth.entity.Comment;
import com.examplejjwt.jwtauth.entity.User;
import com.examplejjwt.jwtauth.exception.BlogNotFoundException;
import com.examplejjwt.jwtauth.exception.UnauthorizedAccessException;
import com.examplejjwt.jwtauth.repository.BlogRepository;
import com.examplejjwt.jwtauth.repository.CommentRepository;
import com.examplejjwt.jwtauth.repository.LikeRepository;
import com.examplejjwt.jwtauth.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class BlogService {
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    public BlogService(UserRepository userRepository, BlogRepository blogRepository, LikeRepository likeRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.blogRepository = blogRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
    }

    public BlogDetailResponse createblog(BlogDto blogDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            String username = ((UserDetails) principal).getUsername();
            User user = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
            Blog newblog = new Blog();
            newblog.setTitle(blogDto.getTitle());
            newblog.setContent(blogDto.getContent());
            newblog.setCategory(blogDto.getCategory());
            newblog.setUser(user);
            Blog blog= blogRepository.save(newblog);
            BlogDetailResponse blogResponseDto = new BlogDetailResponse(
                    blog.getId(),
                    blog.getTitle(),
                    blog.getContent(),
                    blog.getCategory(),
                    username,
                    "Blog create Succesfully"
            );
            return blogResponseDto;
        }
        return null;
    }
    public Page<BlogDetailResponse> getblogs(int page) {
        if (page < 0 ) {
            throw new IllegalArgumentException("Page number must be non-negative ");
        }
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.ASC, "id"));
        Page<Blog> blogs = blogRepository.findAll(pageable);
        return blogs.map(blog -> {
            int likecount = likeRepository.countByBlog(blog).intValue();
            return new BlogDetailResponse(
                    blog.getId(),
                    blog.getTitle(),
                    blog.getContent(),
                    blog.getCategory(),
                    blog.getUser().getUsername(),
                    likecount
            );
        });
    }

    public BlogDetailResponse getsBlogById(Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(()->new BlogNotFoundException("Blog not found"));
        int likecount = likeRepository.countByBlog(blog).intValue();
        List<String> comments = commentRepository.findByBlog(blog).stream().map(Comment::getCont).collect(Collectors.toList());
        BlogDetailResponse blogDetailResponses = new BlogDetailResponse(
                blog.getId(),
                blog.getTitle(),
                blog.getContent(),
                blog.getCategory(),
                blog.getUser().getUsername(),
                likecount,
                comments
        );
        return blogDetailResponses;
    }

    public BlogDetailResponse updateBlog(Long id,BlogDto blogDto) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new BlogNotFoundException("Blog not found"));
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            String username = ((UserDetails) principal).getUsername();
            userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
            if(!username.equals(blog.getUser().getUsername())){
                throw new UnauthorizedAccessException("you have not access for update this Blog");
            }
            if(!blogDto.getTitle().isEmpty()) blog.setTitle(blogDto.getTitle());
            if(!blogDto.getContent().isEmpty()) blog.setContent(blogDto.getContent());
            if(!blogDto.getCategory().isEmpty()) blog.setCategory(blogDto.getCategory());
            BlogDetailResponse blogResponseDto = new BlogDetailResponse(
                    blog.getId(),
                    blog.getTitle(),
                    blog.getContent(),
                    blog.getCategory(),
                    username,
                    "Blog update Succesfully"
            );
            blogRepository.save(blog);
            return blogResponseDto;
        }
        return null;
    }

    public String delteBlog(Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new RuntimeException("Blog not found"));
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            String username = ((UserDetails) principal).getUsername();
            User loginuser = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
            if(!blog.getUser().getUsername().equals(loginuser.getUsername())){
                throw new UnauthorizedAccessException("you have not access for delete this Blog");
            }
            blogRepository.delete(blog);
            return "Blog delete Succesfully";
        }
        return null;
    }

    public Page<BlogDetailResponse> getLoginUserBlogs(int page,int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page number must be non-negative and size must be positive");
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            String username = ((UserDetails) principal).getUsername();
            User loginuser = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
            Pageable pageable = PageRequest.of(page, size);
            Page<Blog> blogs = blogRepository.findByUser(loginuser,pageable);
            return blogs.map(blog -> {
                int likecount = likeRepository.countByBlog(blog).intValue();
                List<String> comments = commentRepository.findByBlog(blog).stream().map(Comment::getCont).collect(Collectors.toList());
                return new BlogDetailResponse(
                        blog.getId(),
                        blog.getTitle(),
                        blog.getContent(),
                        blog.getCategory(),
                        likecount,
                        comments
                );
            });
        }
        return null;
    }

    public Page<BlogDetailResponse> getUserBlogs(String username,int page,int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page number must be non-negative and size must be positive");
        }
        User user = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
        Pageable pageable = PageRequest.of(page, size);
        Page<Blog> blogs = blogRepository.findByUser(user,pageable);
        return blogs.map(blog -> {
            int likecount = likeRepository.countByBlog(blog).intValue();
            List<String> comments = commentRepository.findByBlog(blog).stream().map(Comment::getCont).collect(Collectors.toList());
            return new BlogDetailResponse(
                    blog.getId(),
                    blog.getTitle(),
                    blog.getContent(),
                    blog.getCategory(),
                    likecount,
                    comments
            );
        });
    }
}
