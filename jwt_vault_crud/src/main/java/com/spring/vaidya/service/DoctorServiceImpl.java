package com.spring.vaidya.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.vaidya.entity.ConfirmTokenDoctor;
import com.spring.vaidya.entity.User;
import com.spring.vaidya.repo.ConfirmTokenDoctorRepo;
import com.spring.vaidya.repo.DoctorRepository;



@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ConfirmTokenDoctorRepo confirmTokenDoctorRepo;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> saveDoctor(User doctor) {
        if (doctorRepository.existsByUserEmail(doctor.getUserEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        doctorRepository.save(doctor);

        ConfirmTokenDoctor confirmationToken = new ConfirmTokenDoctor(doctor);
        confirmTokenDoctorRepo.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(doctor.getUserEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                + "http://localhost:8080/doctor/confirm-account?token=" + confirmationToken.getConfirmationToken());
        emailService.sendEmail(mailMessage);

        return ResponseEntity.ok("Verify email by the link sent to your email address");
    }

    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmTokenDoctor token = confirmTokenDoctorRepo.findByConfirmTokenDoctor(confirmationToken);

        if (token != null) {
          User doctor = doctorRepository.findByUserEmailIgnoreCase(token.getDoctorEntity().getUserEmail());
            doctor.setEnabled(true);
            doctorRepository.save(doctor);
            return ResponseEntity.ok("Email verified successfully!");
        }

        return ResponseEntity.badRequest().body("Error: Couldn't verify email");
    }
    @Override
    public User getDoctorByEmail(String email) {
        return doctorRepository.findByUserEmail(email);
    }
    
    
    @Override
    public Optional<User> getUserById(Long id) {
        return doctorRepository.findById(id);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        Optional<User> existingUser = doctorRepository.findById(id);

        if (existingUser.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + id);
        }

        User user = existingUser.get();
        user.setFullName(updatedUser.getFullName());
        user.setUserEmail(updatedUser.getUserEmail());
        user.setSpecialization(updatedUser.getSpecialization());
        user.setQualification(updatedUser.getQualification());
        user.setExperience(updatedUser.getExperience());
        user.setAddress(updatedUser.getAddress());
        user.setGender(updatedUser.getGender());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        user.setPassword(updatedUser.getPassword());
        user.setDiseases(updatedUser.getDiseases());
        user.setClinicName(updatedUser.getClinicName());
        user.setOpenTime(updatedUser.getOpenTime());
        user.setCloseTime(updatedUser.getCloseTime());
        user.setEnabled(updatedUser.isEnabled());
        user.setRoleId(updatedUser.getRoleId());
        user.setAadharNo(updatedUser.getAadharNo());

        return doctorRepository.save(user);
    }
}
