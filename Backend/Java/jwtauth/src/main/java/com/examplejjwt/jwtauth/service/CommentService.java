package com.examplejjwt.jwtauth.service;

import com.examplejjwt.jwtauth.entity.Blog;
import com.examplejjwt.jwtauth.entity.Comment;
import com.examplejjwt.jwtauth.entity.User;
import com.examplejjwt.jwtauth.exception.BlogNotFoundException;
import com.examplejjwt.jwtauth.repository.BlogRepository;
import com.examplejjwt.jwtauth.repository.CommentRepository;
import com.examplejjwt.jwtauth.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public CommentService(BlogRepository blogRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public String addcomment(Long blogId, String cont) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new BlogNotFoundException("Blog not found"));
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principle instanceof UserDetails){
            String username = ((UserDetails) principle).getUsername();
            User loginuser = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
            Comment comment = new Comment(null,cont,loginuser,blog);
            commentRepository.save(comment);
            return "Comment Added Successfully";
        }
        return null;
    }

    public String deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new BlogNotFoundException("Blog not found"));
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principle instanceof UserDetails){
            String username = ((UserDetails) principle).getUsername();
            User loginuser = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
            if(!comment.getUser().getId().equals(loginuser.getId())){
                return "Only the comment author delete the comment";
            }
            commentRepository.delete(comment);
            return "Comment delete successfully";
        }
        return null;
    }
}
