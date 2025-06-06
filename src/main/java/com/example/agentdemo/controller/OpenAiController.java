package com.example.agentdemo.controller;

import com.example.agentdemo.dto.ChatRequest;
import com.example.agentdemo.dto.ChatResponse;
import com.example.agentdemo.service.OpenAiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/openai")
public class OpenAiController {

    @Autowired
    private OpenAiService openAiService;

    @PostMapping("/chat")
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        String answer = openAiService.chat(request.getMessage());
        return new ChatResponse(answer);
    }
}
