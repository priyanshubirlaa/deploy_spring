package com.spring.vaidya.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.vaidya.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //Optional<User> findByUsername(String username);

    Optional<User> findByUserEmailIgnoreCase(String email);
}
