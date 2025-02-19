package com.spring.vaidya.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.spring.vaidya.entity.User;


public interface DoctorService {
	ResponseEntity<?> saveDoctor(User doctor);

    ResponseEntity<?> confirmEmail(String confirmTokenDoctor);
    
    User getDoctorByEmail(String email);
    
    Optional<User> getUserById(Long id);
    User updateUser(Long id, User user);
}
