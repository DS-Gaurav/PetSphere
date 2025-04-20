//package com.examplejjwt.jwtauth.controller;
//
//import com.examplejjwt.jwtauth.dto.AuthRequest;
//import com.examplejjwt.jwtauth.dto.RegisterRequest;
//import com.examplejjwt.jwtauth.entity.User;
//import com.examplejjwt.jwtauth.repository.UserRepository;
//import com.examplejjwt.jwtauth.security.JwtUtil;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//    private final UserRepository userRepository;
//    private  final PasswordEncoder passwordEncoder;
//    private final AuthenticationManager authenticationManager;
//    private final JwtUtil jwtUtil;
//
//    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.authenticationManager = authenticationManager;
//        this.jwtUtil = jwtUtil;
//    }
//
//    @PostMapping("/register")
//    public Map<String,String> register(@RequestBody RegisterRequest request){
//        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
//        if(existingUser.isPresent()){
//            return Map.of("message","Username already taken");
//        }
//        User user = new User();
//        user.setUsername(request.getUsername());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setRoles(request.getRoles());
//
//        userRepository.save(user);
//        return Map.of("message", "User registered successfully");
//    }
//
//    @PostMapping("/login")
//    public Map<String,String> login(@RequestBody AuthRequest request){
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
//        String token = jwtUtil.generateToken(request.getUsername());
//        return Map.of("token",token);
//    }
//}
package com.examplejjwt.jwtauth.controller;

import com.examplejjwt.jwtauth.dto.AuthRequest;
import com.examplejjwt.jwtauth.dto.RegisterRequest;
import com.examplejjwt.jwtauth.entity.Role;
import com.examplejjwt.jwtauth.entity.User;
import com.examplejjwt.jwtauth.repository.UserRepository;
import com.examplejjwt.jwtauth.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5174")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private Logger logger = LoggerFactory.getLogger(AuthController.class);
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody RegisterRequest request) {
        logger.info("Click on the register api");
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            logger.info("user already present");
            return Map.of("message", "Username already taken");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(request.getRoles() != null ? request.getRoles() : Set.of(Role.USER));

        userRepository.save(user);
        logger.info("user register succesfully");
        String token = jwtUtil.generateToken(user);
        return Map.of("message", "User registered successfully","token", token);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(()-> new RuntimeException("User not found"));
        if(user.isGlogin()){
            throw new RuntimeException("This account was created using Google Login. Please use Google Sign-In.");
        }
        logger.info("In login api");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        String token = jwtUtil.generateToken(user);
//        return Map.of("message", "User Login successfully");
        logger.info("login succesfully");
        return Map.of("token", token);
    }
}

