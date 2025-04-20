package com.examplejjwt.jwtauth.service;

import com.examplejjwt.jwtauth.entity.Blog;
import com.examplejjwt.jwtauth.entity.Like;
import com.examplejjwt.jwtauth.entity.User;
import com.examplejjwt.jwtauth.exception.BlogNotFoundException;
import com.examplejjwt.jwtauth.repository.BlogRepository;
import com.examplejjwt.jwtauth.repository.LikeRepository;
import com.examplejjwt.jwtauth.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class LikeService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    public LikeService(BlogRepository blogRepository, UserRepository userRepository, LikeRepository likeRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
    }

    public String createLike(Long blogId) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new BlogNotFoundException("Blog not found"));
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principle instanceof UserDetails){
            String username = ((UserDetails) principle).getUsername();
            User loginuser = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
            Optional<Like> bloglike = likeRepository.findByUserAndBlog(loginuser,blog);
            if(bloglike.isPresent()){
                likeRepository.delete(bloglike.get());
                return "Blog Dislike successfully";
            }
            else{
                likeRepository.save(new Like(null,loginuser,blog));
                return "Blog like successfully";
            }
        }
        return null;
    }
}
