package com.examplejjwt.jwtauth.repository;

import com.examplejjwt.jwtauth.entity.Pet;
import com.examplejjwt.jwtauth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet,Long> {
    Page<Pet> findAll(Pageable pageable);
    Page<Pet> findBySellTrue(Pageable pageable);
    Page<Pet> findByUser(User user, Pageable pageable);
}
