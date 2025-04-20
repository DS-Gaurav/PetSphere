package com.examplejjwt.jwtauth.controller;

import com.examplejjwt.jwtauth.dto.BlogDetailResponse;
import com.examplejjwt.jwtauth.dto.BlogDto;
import com.examplejjwt.jwtauth.dto.BlogDetailResponse;
import com.examplejjwt.jwtauth.repository.BlogRepository;
import com.examplejjwt.jwtauth.service.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
public class BlogController {
    private final BlogRepository blogRepository;
    private final BlogService service;
    private Logger logger = LoggerFactory.getLogger(BlogController.class);
    public BlogController(BlogRepository blogRepository, BlogService service) {
        this.blogRepository = blogRepository;
        this.service = service;
    }
    @PostMapping("/blogs")
    public ResponseEntity<?> createblog(@RequestBody BlogDto blogDto){
        if (blogDto.getId() != null && blogRepository.existsById(blogDto.getId())) {
            logger.error("In create blog api blog id already present");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Blog with this ID already exists");
        }
        BlogDetailResponse responseDto = service.createblog(blogDto);
        if(responseDto == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        logger.info("Blog created");
        return ResponseEntity.ok(responseDto);
    }
    @GetMapping("/allblogs")
    public ResponseEntity<?> getallblogs(@RequestParam(defaultValue = "0") int page){
        logger.info("from all blogs api find all blogs");
        return ResponseEntity.ok(service.getblogs(page));
    }

    @GetMapping("/allblogs/{id}")
    public ResponseEntity<?> getblogbyid(@PathVariable Long id){
        return ResponseEntity.ok(service.getsBlogById(id));
    }

    @GetMapping("/myblogs")
    public ResponseEntity<?> getblogbyuser(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size){
        Page<BlogDetailResponse> blogDetailResponses = service.getLoginUserBlogs(page,size);
        if(blogDetailResponses == null){
            logger.error("you are unauthorize ");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        return ResponseEntity.ok(blogDetailResponses);
    }

    @GetMapping("/blogs/{username}")
    public ResponseEntity<?> getblogbyuser(@PathVariable String username,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size){
        return ResponseEntity.ok(service.getUserBlogs(username,page,size));
    }

    @PutMapping("/blogs/{id}")
    public ResponseEntity<?> updateblogbyid(@PathVariable Long id,@RequestBody BlogDto blogDto){
        BlogDetailResponse BlogDetailResponse = service.updateBlog(id,blogDto);
        if(BlogDetailResponse == null){
            logger.error("unauthorize user not update");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        return ResponseEntity.ok(BlogDetailResponse);
    }


    @DeleteMapping("/blogs/{id}")
    public ResponseEntity<String> deletebyid(@PathVariable Long id){
        String response = service.delteBlog(id);
        if(response == null){
            logger.error("unauthorize user not Blog delete ");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        logger.info("blog delete");
        return ResponseEntity.ok(response);
    }
}
