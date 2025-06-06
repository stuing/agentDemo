package com.example.agentdemo.service;

import com.example.agentdemo.entity.Account;
import com.example.agentdemo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import org.springframework.util.StringUtils;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account registerAccount(Account account) {
        if (!StringUtils.hasText(account.getUsername()) || !StringUtils.hasText(account.getPassword())) {
            throw new IllegalArgumentException("用户名和密码不能为空");
        }

        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new IllegalArgumentException("该用户名已存在");
        }

        if (account.getEmail() != null && accountRepository.findByEmail(account.getEmail()).isPresent()) {
            throw new IllegalArgumentException("该邮箱已被注册");
        }

        if (account.getPhone() != null && accountRepository.findByPhone(account.getPhone()).isPresent()) {
            throw new IllegalArgumentException("该手机号已被注册");
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setCreatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }

    public Account login(String username, String password) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new IllegalArgumentException("用户名和密码不能为空");
        }

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("用户名或密码错误"));

        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }

        return account;
    }
}
