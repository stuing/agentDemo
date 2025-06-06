package com.example.agentdemo.service;

import com.example.agentdemo.entity.Account;
import com.example.agentdemo.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AccountService accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setUsername("user1");
        account.setPassword("rawpass");
    }

    @Test
    void registerAccount_success() {
        when(accountRepository.findByUsername("user1")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("rawpass")).thenReturn("encoded");
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account saved = accountService.registerAccount(account);

        assertEquals("encoded", saved.getPassword());
        assertNotNull(saved.getCreatedAt());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void registerAccount_missingUsernameOrPassword() {
        Account invalid = new Account();
        assertThrows(IllegalArgumentException.class, () -> accountService.registerAccount(invalid));
    }

    @Test
    void registerAccount_duplicateUsername() {
        when(accountRepository.findByUsername("user1")).thenReturn(Optional.of(new Account()));
        assertThrows(IllegalArgumentException.class, () -> accountService.registerAccount(account));
    }

    @Test
    void registerAccount_duplicateEmail() {
        account.setEmail("test@example.com");
        when(accountRepository.findByUsername("user1")).thenReturn(Optional.empty());
        when(accountRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new Account()));
        assertThrows(IllegalArgumentException.class, () -> accountService.registerAccount(account));
    }

    @Test
    void login_success() {
        when(accountRepository.findByUsername("user1")).thenReturn(Optional.of(account));
        when(passwordEncoder.matches("rawpass", "rawpass")).thenReturn(true);

        Account result = accountService.login("user1", "rawpass");

        assertEquals(account, result);
    }

    @Test
    void login_wrong_password() {
        when(accountRepository.findByUsername("user1")).thenReturn(Optional.of(account));
        when(passwordEncoder.matches("wrong", "rawpass")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> accountService.login("user1", "wrong"));
    }
}
