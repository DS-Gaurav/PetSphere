package com.examplejjwt.jwtauth.controller;

import com.examplejjwt.jwtauth.dto.GoogleAuthSecret;
import com.examplejjwt.jwtauth.entity.Role;
import com.examplejjwt.jwtauth.entity.User;
import com.examplejjwt.jwtauth.repository.UserRepository;
import com.examplejjwt.jwtauth.security.JwtUtil;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.util.*;

@RestController
@RequestMapping("/auth/google")
public class GoogleAuthController {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final GoogleAuthSecret googleAuthSecret;
    private final AuthenticationManager authenticationManager;
    public GoogleAuthController(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, GoogleAuthSecret googleAuthSecret, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.googleAuthSecret = googleAuthSecret;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/callback")
    public ResponseEntity<?> googlecallback(@RequestParam String code){
        try{
            String tokenendpoint = "https://oauth2.googleapis.com/token";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
            params.add("code",code);
            params.add("client_id", googleAuthSecret.getClientId());
            params.add("client_secret", googleAuthSecret.getClientSecret());
            params.add("redirect_uri","https://developers.google.com/oauthplayground");
            params.add("grant_type","authorization_code");

            HttpEntity<MultiValueMap <String,String>> request = new HttpEntity<>(params,headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenendpoint, request, Map.class);
            String accessToken = (String) response.getBody().get("access_token");

            HttpHeaders userHeaders = new HttpHeaders();
            userHeaders.setBearerAuth(accessToken);
            HttpEntity<String> userRequest = new HttpEntity<>(userHeaders);

            ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v2/userinfo",
                    HttpMethod.GET,
                    userRequest,
                    Map.class
            );
            Map<String, Object> userInfo = userInfoResponse.getBody();
            String email = (String) userInfo.get("email");
            String password = UUID.randomUUID().toString();
            Optional<User> existingUser = userRepository.findByUsername(email);
            User newUser = new User();
            if (existingUser.isEmpty()) {
                newUser.setUsername(email);
                newUser.setPassword(passwordEncoder.encode(password));
                newUser.setRoles(Set.of(Role.USER));
                newUser.setGlogin(true);
                userRepository.save(newUser);
            }
            else{
                Map<String, String> response1 = new HashMap<>();
                response1.put("message","username already present");
                response1.put("username", email);
                return ResponseEntity.ok(response1);
            }
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
            String jwtToken = jwtUtil.generateToken(newUser);
            Map<String, String> response1 = new HashMap<>();
            response1.put("message","user login succesfully");
            response1.put("token", jwtToken);
            response1.put("username", email);

            return ResponseEntity.ok(response1);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during Google OAuth: " + e.getMessage());
        }
    }
}
