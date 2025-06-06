package com.example.agentdemo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class OpenAiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url:https://api.openai.com/v1/chat/completions}")
    private String apiUrl;

    @Value("${openai.model:gpt-3.5-turbo}")
    private String model;

    public OpenAiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String chat(String message) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> request = new HashMap<>();
            request.put("model", model);
            request.put("messages", Collections.singletonList(
                    Collections.singletonMap("role", "user")
            ));
            ((java.util.List<Map<String, String>>) request.get("messages")).get(0).put("content", message);

            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(request), headers);
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

            JsonNode node = objectMapper.readTree(response.getBody());
            return node.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to call OpenAI API", e);
        }
    }
}
