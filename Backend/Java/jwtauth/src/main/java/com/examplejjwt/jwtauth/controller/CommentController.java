package com.examplejjwt.jwtauth.controller;

import com.examplejjwt.jwtauth.repository.CommentRepository;
import com.examplejjwt.jwtauth.repository.UserRepository;
import com.examplejjwt.jwtauth.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/blogs")
public class CommentController {
    private final CommentService service;
    public CommentController(CommentService service) {
        this.service = service;
    }
    @PostMapping("/{blog_id}/comment")
    public ResponseEntity<?> addComment(@PathVariable Long blog_id, @RequestBody String cont){
        String response = service.addcomment(blog_id,cont);
        if(response == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorize");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/comment/{comment_id}")
    public ResponseEntity<String> deletecomment(@PathVariable Long comment_id){
        String response = service.deleteComment(comment_id);
        if(response == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorize");
        return ResponseEntity.ok(response);
    }
}
