package com.tveritin.controller;

import com.tveritin.bankservice.api.TransfersApi;
import com.tveritin.bankservice.dto.TransferRequest;
import com.tveritin.bankservice.dto.TransferResponse;
import com.tveritin.exception.InsufficientFundsException;
import com.tveritin.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferController implements TransfersApi {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }
    @Override
    public ResponseEntity<TransferResponse> transfersPost(TransferRequest transferRequest) {
        TransferResponse response;
        try {
            response = transferService.transferFunds(transferRequest);
        } catch (InsufficientFundsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); //todo more info
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);    }
}
