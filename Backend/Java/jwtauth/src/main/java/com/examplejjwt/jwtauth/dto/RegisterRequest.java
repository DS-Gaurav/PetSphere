package com.examplejjwt.jwtauth.dto;

import com.examplejjwt.jwtauth.entity.Role;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private Set<Role> roles;
}
