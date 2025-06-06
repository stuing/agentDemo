package com.example.agentdemo.service;

import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.stereotype.Service;

@Service
public class OpenAiService {

    private final OpenAiChatClient chatClient;

    public OpenAiService(OpenAiChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String chat(String message) {
        return chatClient.call(message).getResult().getOutput().getContent();
    }
}
