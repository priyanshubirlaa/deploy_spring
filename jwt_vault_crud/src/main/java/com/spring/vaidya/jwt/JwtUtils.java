package com.spring.vaidya.jwt;


import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultKeyValueOperationsSupport.KeyValueBackend;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import com.spring.vaidya.service.HashiCorpApiService;
import com.spring.vaidya.service.TokenService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils implements CommandLineRunner {
	
	
    private String jwtSecret; // Declare jwtSecret as an instance variable
    private final int jwtExpirationMs = 86400000;

    @Autowired
    private VaultTemplate vaultTemplate;
    
    @Autowired
    private  TokenService tokenService;
    
    @Autowired
    private HashiCorpApiService hashicorpApiService;

    // Method to generate JWT token
    public String generateJwtToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // Use the jwtSecret instance variable
                .compact();
    }

    // Method to validate JWT token
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token); // Use the jwtSecret instance variable
            return true;
        } catch (JwtException e) {
            System.err.println("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }

    // Method to extract username from JWT token
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject(); // Use the jwtSecret instance variable
    }

    // Method to fetch JWT secret from Vault
    @Override
    public void run(String... strings) throws Exception {
    	tokenService.refreshAccessToken(); // Ensure the token is refreshed first
        jwtSecret = hashicorpApiService.callHashiCorpApi();
        System.out.println("JWT Secret: " + jwtSecret);

    }
}