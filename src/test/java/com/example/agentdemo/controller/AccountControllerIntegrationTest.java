package com.example.agentdemo.controller;

import com.example.agentdemo.entity.Account;
import com.example.agentdemo.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void createAccount_success() throws Exception {
        String json = "{\"username\":\"alice\",\"password\":\"secret\"}";
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void createAccount_duplicateUsername() throws Exception {
        Account account = new Account();
        account.setUsername("bob");
        account.setPassword("pwd");
        accountRepository.save(account);

        String json = "{\"username\":\"bob\",\"password\":\"another\"}";
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void login_success() throws Exception {
        Account acc = new Account();
        acc.setUsername("tom");
        acc.setPassword(passwordEncoder.encode("secret"));
        accountRepository.save(acc);

        String json = "{\"username\":\"tom\",\"password\":\"secret\"}";
        mockMvc.perform(post("/api/accounts/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("tom"));
    }

    @Test
    void login_wrongPassword() throws Exception {
        Account acc = new Account();
        acc.setUsername("mary");
        acc.setPassword(passwordEncoder.encode("pwd"));
        accountRepository.save(acc);

        String json = "{\"username\":\"mary\",\"password\":\"bad\"}";
        mockMvc.perform(post("/api/accounts/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isInternalServerError());
    }
}
