package com.spring.vaidya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.spring.vaidya.entity.AuthRequest;
import com.spring.vaidya.entity.LoginRequest;
import com.spring.vaidya.entity.LoginResponse;
import com.spring.vaidya.entity.User;
import com.spring.vaidya.jwt.JwtUtils;
import com.spring.vaidya.repo.UserRepository;
import com.spring.vaidya.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/new")
    public String registerUser(@RequestBody User user) {
        userService.registerUser(user);
        return "User registered successfully!";
    }

    @PostMapping("/authenticate")
    public String authenticateUser(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authRequest.getUsername());
        return "Bearer " + jwt;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the JWT-secured API!";
    }
    
    @GetMapping("/protected")
    public String protectedRoute() {
        return "This is a protected route, only accessible with a valid JWT.";
    }
    

    @PostMapping("/login")
    public ResponseEntity<?> loginDoctor(@RequestBody LoginRequest loginRequest) {
        // Fetch the doctor by email
        User doctor = userRepository.findByUserEmailIgnoreCase(loginRequest.getUserEmail())
                .orElse(null);

        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"); // Doctor not found
        }

        // Check if the passwords match
        boolean isPasswordMatch = passwordEncoder.matches(loginRequest.getPassword(), doctor.getPassword());

        if (!isPasswordMatch) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"); // Passwords don't match
        }

        if (!doctor.isEnabled()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Doctor is not verified"); // Doctor is not verified
        }

        // Generate JWT token
        String jwt = jwtUtils.generateJwtToken(doctor.getUserEmail());

        // Create a response object to include the JWT and essential user details
        LoginResponse response = new LoginResponse(
             jwt,
            doctor.getFullName(),
            doctor.getUserId()
        );

        return ResponseEntity.ok(response); // Return token and selected user details
    }
}
