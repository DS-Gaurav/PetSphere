package com.examplejjwt.jwtauth.repository;

import com.examplejjwt.jwtauth.entity.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,Long> {
    boolean existsByMobile(String mobile);
    boolean existsByEmail(String email);
    boolean existsByUserId(Long userId);
    Optional<Profile> findByUserId(Long userId);
    Optional<Profile> findByMobile(String mobile);
    @Modifying
    @Transactional
    @Query("DELETE FROM Profile p WHERE p.mobile = :mobile")
    void deleteByMobile(String mobile);
}
