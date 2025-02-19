package com.tveritin.service;

import com.tveritin.bankservice.dto.TransferRequest;
import com.tveritin.bankservice.dto.TransferResponse;
import com.tveritin.entity.Account;
import com.tveritin.exception.InsufficientFundsException;
import com.tveritin.exception.ResourceNotFoundException;
import com.tveritin.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransferService {

    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public TransferResponse transferFunds(TransferRequest transferRequest) throws InsufficientFundsException {
        Account fromAccount = accountRepository.findByAccountNumber(transferRequest.getFromAccount())
                .orElseThrow(() -> new ResourceNotFoundException("From account not found"));
        Account toAccount = accountRepository.findByAccountNumber(transferRequest.getToAccount())
                .orElseThrow(() -> new ResourceNotFoundException("To account not found"));

        if (fromAccount.getBalance() < transferRequest.getAmount() + transferRequest.getFee()) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        fromAccount.setBalance(fromAccount.getBalance() - transferRequest.getAmount() - transferRequest.getFee());
        toAccount.setBalance(toAccount.getBalance() + transferRequest.getAmount());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        TransferResponse response = new TransferResponse();
        response.setTransactionId(UUID.randomUUID().toString());
        response.setRemainingBalance(fromAccount.getBalance());

        return response;
    }
}
