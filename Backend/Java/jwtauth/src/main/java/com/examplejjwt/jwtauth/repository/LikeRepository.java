package com.examplejjwt.jwtauth.repository;

import com.examplejjwt.jwtauth.entity.Blog;
import com.examplejjwt.jwtauth.entity.Like;
import com.examplejjwt.jwtauth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {
    Optional<Like> findByUserAndBlog(User user, Blog blog);
    Long countByBlog(Blog blog);
}
