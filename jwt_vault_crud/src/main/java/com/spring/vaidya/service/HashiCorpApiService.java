package com.spring.vaidya.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;

@Service
public class HashiCorpApiService {

    private final TokenService tokenService;

    @Value("${hashicorp.apiUrl}")
    private String apiUrl;

    public HashiCorpApiService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public String callHashiCorpApi() {  // Change return type to String
        String accessToken = tokenService.getAccessToken();
        System.out.println("Using Access Token: " + accessToken); 
        String url = apiUrl;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
        	 String secretValue = extractSecretValue(response.getBody());  // Store output in a variable
             System.out.println("Stored Secret Value: " + secretValue);  // Debugging purpose
             return secretValue;  // Return extracted value
        } else {
            throw new RuntimeException("Failed to call HashiCorp API");
        }
    }

    private String extractSecretValue(String responseBody) {  // Change return type to String
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            return rootNode.path("secrets").get(0).path("static_version").path("value").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON response", e);
        }
    }
}
