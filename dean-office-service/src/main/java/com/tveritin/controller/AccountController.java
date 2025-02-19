package com.tveritin.controller;

import com.tveritin.bankservice.api.AccountsApi;
import com.tveritin.bankservice.dto.CloseAccountRequest;
import com.tveritin.bankservice.dto.OpenAccountRequest;
import com.tveritin.bankservice.dto.OpenAccountResponse;
import com.tveritin.entity.Account;
import com.tveritin.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController implements AccountsApi {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public ResponseEntity<Void> accountsDelete(CloseAccountRequest closeAccountRequest) {
        accountService.closeAccount(closeAccountRequest.getAccountNumber());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<OpenAccountResponse> accountsPost(OpenAccountRequest openAccountRequest) {
        Account account = accountService.openAccount(openAccountRequest);
        OpenAccountResponse response = new OpenAccountResponse();
        response.setAccountId(account.getAccountNumber());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
