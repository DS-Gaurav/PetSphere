package com.examplejjwt.jwtauth.repository;

import com.examplejjwt.jwtauth.entity.Blog;
import com.examplejjwt.jwtauth.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByBlog(Blog blog);
}
