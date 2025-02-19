package com.spring.vaidya.controller;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.vaidya.entity.User;
import com.spring.vaidya.repo.DoctorRepository;
import com.spring.vaidya.service.DoctorServiceImpl;

@RestController
@RequestMapping("/doctor")
@CrossOrigin(origins = "http://localhost:5173/")
public class DoctorController {

    @Autowired
    private DoctorServiceImpl doctorService;

    @PostMapping("/register")

    @CrossOrigin(origins = "http://localhost:5173/")
    public ResponseEntity<?> registerDoctor(@RequestBody User doctor) {
        return doctorService.saveDoctor(doctor);
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<?> confirmDoctorAccount(@RequestParam("token") String confirmationToken) {
        return doctorService.confirmEmail(confirmationToken);
    }
    
    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping("/all")
    public List<User> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<User> getDoctorById(@PathVariable Long doctorId) {
        Optional<User> doctor = doctorRepository.findById(doctorId);
        if (doctor.isPresent()) {
            return ResponseEntity.ok(doctor.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

	/*
	 * @GetMapping("/email") public Doctor getDoctorByEmail(@RequestParam String
	 * email) { return doctorRepository.findByUserEmail(email); }
	 */
    @GetMapping("/email")
    public ResponseEntity<User> getDoctorByEmail(@RequestParam String email) {
        User doctor = doctorService.getDoctorByEmail(email);
        if (doctor != null) {
            return ResponseEntity.ok(doctor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> existingUser = doctorService.getUserById(id);

        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
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

        User savedUser = doctorService.updateUser(id, user);
        return ResponseEntity.ok(savedUser);
    }
}
