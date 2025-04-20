package com.examplejjwt.jwtauth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogDetailResponse {
    public Long id;
    public String title;
    public String content;
    public String category;
    public String username;
    public int likes;
    public List<String> comments;
    public String message;

    public BlogDetailResponse(Long id,String title, String content, String category, String username, int likecount) {
        this.id = id;
        this.title = title;
        this.content= content;
        this.category = category;
        this.username=username;
        this.likes = likecount;
    }

    public BlogDetailResponse(Long id,String title, String content, String category, int likecount, List<String> comments) {
        this.id=id;
        this.title=title;
        this.content = content;
        this.category= category;
        this.likes=likecount;
        this.comments=comments;
    }

    public BlogDetailResponse(Long id, String title, String content, String category, String username, String message) {
        this.id=id;
        this.title=title;
        this.content=content;
        this.category=category;
        this.username=username;
        this.message=message;
    }

    public BlogDetailResponse(Long id, String title, String content, String category, String username, int likecount, List<String> comments) {
        this.id=id;
        this.title=title;
        this.content=content;
        this.category=category;
        this.username=username;
        this.likes=likecount;
        this.comments=comments;
    }
}
