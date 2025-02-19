package com.spring.vaidya.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.vaidya.entity.User;


public interface DoctorRepository extends JpaRepository<User, Long>{
	 User findByUserEmailIgnoreCase(String emailId);

	    Boolean existsByUserEmail(String email);
	    
	    User findByUserEmail(String email);

		//Doctor findByFullName(String doctorName);
		  Optional<User> findByFullName(String fullName);
	

}
