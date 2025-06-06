package com.example.agentdemo.controller;

import com.example.agentdemo.entity.Account;
import com.example.agentdemo.dto.LoginRequest;
import com.example.agentdemo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public Account createAccount(@Valid @RequestBody Account account) {
        return accountService.registerAccount(account);
    }

    @PostMapping("/login")
    public Account login(@Valid @RequestBody LoginRequest request) {
        return accountService.login(request.getUsername(), request.getPassword());
    }
}
