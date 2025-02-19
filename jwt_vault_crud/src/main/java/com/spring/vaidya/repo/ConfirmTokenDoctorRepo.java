package com.spring.vaidya.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.vaidya.entity.ConfirmTokenDoctor;


@Repository("confirmTokenDoctorRepo")
public interface ConfirmTokenDoctorRepo extends JpaRepository<ConfirmTokenDoctor, Long> {

    ConfirmTokenDoctor findByConfirmTokenDoctor(String ConfirmTokenDoctor);

}
