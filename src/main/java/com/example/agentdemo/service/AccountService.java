package com.example.agentdemo.service;

import com.example.agentdemo.entity.Account;
import com.example.agentdemo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account registerAccount(Account account) {
        if (account.getUsername() == null || account.getPassword() == null) {
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

        account.setCreatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }
}
