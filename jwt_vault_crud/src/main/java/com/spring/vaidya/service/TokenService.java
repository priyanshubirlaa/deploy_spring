package com.spring.vaidya.service;



import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.concurrent.atomic.AtomicReference;
import java.util.Map;

@Service
public class TokenService {

    private static final String CLIENT_ID = "rRn0jXxVGd3qLqQfLCJn39xTa0hM2hAY"; // Your client ID
    private static final String CLIENT_SECRET = "_KMaWsYp6VRHDA33y8rd2DLBmiplvozitXCbk6vYyMNIsDxTglODs8o0xTlVd77O"; // Your client secret
    private static final String TOKEN_URL = "https://auth.idp.hashicorp.com/oauth2/token";
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    // Store the token in memory using AtomicReference to safely handle updates
    private final AtomicReference<String> accessToken = new AtomicReference<>("");

    // Fetch and store the access token
    public void refreshAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = "client_id=" + CLIENT_ID +
                             "&client_secret=" + CLIENT_SECRET +
                             "&grant_type=client_credentials" +
                             "&audience=https://api.hashicorp.cloud";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send the POST request to get the token
        ResponseEntity<Map> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, requestEntity, Map.class);

        if (response.getBody() != null && response.getBody().containsKey("access_token")) {
            String token = response.getBody().get("access_token").toString();
            accessToken.set(token); // Save token in AtomicReference
            // Print the token to the console
            System.out.println("🔄 Token refreshed successfully! Current Token: " + token);
            System.out.println("🔄 Token refreshed successfully!");
        } else {
            System.err.println("❌ Failed to refresh token!");
        }
    }
    
    
    

    // Scheduler to refresh token every 55 minutes (before expiration)
    @Scheduled(fixedRate = 5* 60 * 1000) // 55 minutes
    public void scheduledTokenRefresh() {
        refreshAccessToken();
    }

    // Method to get the latest access token
    public String getAccessToken() {
        return accessToken.get();
    }
}
