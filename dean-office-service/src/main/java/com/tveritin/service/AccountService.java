package com.tveritin.service;

import com.tveritin.bankservice.dto.OpenAccountRequest;
import com.tveritin.entity.Account;
import com.tveritin.entity.User;
import com.tveritin.exception.ResourceNotFoundException;
import com.tveritin.repository.AccountRepository;
import com.tveritin.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Account openAccount(OpenAccountRequest openAccountRequest) {
        User user = userRepository.findById(UUID.fromString(openAccountRequest.getUserId()))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Account account = new Account();
        account.setAccountNumber(openAccountRequest.getAccount().getAccountNumber());
        account.setCurrency(openAccountRequest.getAccount().getCurrency());
        account.setBalance(openAccountRequest.getAccount().getBalance());
        account.setUser(user);

        return accountRepository.save(account);
    }

    public void closeAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        accountRepository.delete(account);
    }
}
