package com.examplejjwt.jwtauth.repository;

import com.examplejjwt.jwtauth.entity.Blog;
import com.examplejjwt.jwtauth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Long> {
    Page<Blog> findAll(Pageable pageable);
    Page<Blog> findByUser(User user,Pageable pageable);
}
