package com.examplejjwt.jwtauth.dto;

import lombok.Data;

@Data
public class ProfileRequest {
    private Long id;
    private String mobile;
    private String email;
    private Integer age;
    private String username;

    public ProfileRequest(Long id, String mobile, String email, int age, String username) {
        this.id=id;
        this.mobile=mobile;
        this.email=email;
        this.age=age;
        this.username=username;
    }
}
