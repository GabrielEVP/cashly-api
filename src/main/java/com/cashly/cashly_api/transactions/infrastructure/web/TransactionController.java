package com.cashly.cashly_api.transactions.infrastructure.web;

import com.cashly.cashly_api.transactions.application.dto.CreateTransactionRequest;
import com.cashly.cashly_api.transactions.application.dto.TransactionResponse;
import com.cashly.cashly_api.transactions.application.dto.UpdateTransactionRequest;
import com.cashly.cashly_api.transactions.application.usecases.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final GetTransactionByIdUseCase getTransactionByIdUseCase;
    private final GetTransactionsByUserUseCase getTransactionsByUserUseCase;
    private final GetTransactionsByAccountUseCase getTransactionsByAccountUseCase;
    private final UpdateTransactionStatusUseCase updateTransactionStatusUseCase;
    private final CancelTransactionUseCase cancelTransactionUseCase;

    public TransactionController(CreateTransactionUseCase createTransactionUseCase,
                                 GetTransactionByIdUseCase getTransactionByIdUseCase,
                                 GetTransactionsByUserUseCase getTransactionsByUserUseCase,
                                 GetTransactionsByAccountUseCase getTransactionsByAccountUseCase,
                                 UpdateTransactionStatusUseCase updateTransactionStatusUseCase,
                                 CancelTransactionUseCase cancelTransactionUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.getTransactionByIdUseCase = getTransactionByIdUseCase;
        this.getTransactionsByUserUseCase = getTransactionsByUserUseCase;
        this.getTransactionsByAccountUseCase = getTransactionsByAccountUseCase;
        this.updateTransactionStatusUseCase = updateTransactionStatusUseCase;
        this.cancelTransactionUseCase = cancelTransactionUseCase;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody CreateTransactionRequest request) {
        TransactionResponse response = createTransactionUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable String id) {
        TransactionResponse response = getTransactionByIdUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getTransactionsByUser(@RequestParam String userId) {
        List<TransactionResponse> responses = getTransactionsByUserUseCase.execute(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAccount(@PathVariable String accountId) {
        List<TransactionResponse> responses = getTransactionsByAccountUseCase.execute(accountId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransactionStatus(@PathVariable String id,
                                                                       @RequestBody UpdateTransactionRequest request) {
        TransactionResponse response = updateTransactionStatusUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<TransactionResponse> cancelTransaction(@PathVariable String id) {
        TransactionResponse response = cancelTransactionUseCase.execute(id);
        return ResponseEntity.ok(response);
    }
}
