package com.examplejjwt.jwtauth.controller;

import com.examplejjwt.jwtauth.entity.User;
import com.examplejjwt.jwtauth.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        Optional<User> user=userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateuser(@PathVariable Long id,@RequestBody User updateuser){
        return userRepository.findById(id)
                .map(user ->{
                    if(userRepository.existsByUsername(updateuser.getUsername()) && !user.getUsername().equals(updateuser.getUsername())){
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Username Already exit");
                    }
                    user.setUsername(updateuser.getUsername());
                    user.setPassword(passwordEncoder.encode(updateuser.getPassword()));
                    return ResponseEntity.ok(userRepository.save(user));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteuser(@PathVariable Long id){
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
