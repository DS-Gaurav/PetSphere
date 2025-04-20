package com.examplejjwt.jwtauth.controller;

import com.examplejjwt.jwtauth.entity.Blog;
import com.examplejjwt.jwtauth.repository.BlogRepository;
import com.examplejjwt.jwtauth.repository.LikeRepository;
import com.examplejjwt.jwtauth.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/blogs/likes")
public class LikeController {
    private final LikeRepository likeRepository;
    private final BlogRepository blogRepository;
    private final LikeService service;

    public LikeController(LikeRepository likeRepository, BlogRepository blogRepository, LikeService service) {
        this.likeRepository = likeRepository;
        this.blogRepository = blogRepository;
        this.service = service;
    }
    @PostMapping("/{blog_id}")
    public ResponseEntity<?> createlikes(@PathVariable Long blog_id){
        String response = service.createLike(blog_id);
        if(response == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorize");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{blog_id}/count")
    public ResponseEntity<Long> bloglikecount(@PathVariable Long blog_id){
        Blog blog = blogRepository.findById(blog_id).orElseThrow(() -> new RuntimeException("Blog not found"));
        long likecount = likeRepository.countByBlog(blog);
        return ResponseEntity.ok(likecount);
    }

}
