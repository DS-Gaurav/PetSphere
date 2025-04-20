package com.examplejjwt.jwtauth.dto;

import lombok.Data;

@Data
public class BlogDto {
    public Long id;
    public String title;
    public String content;
    public String category;
}
