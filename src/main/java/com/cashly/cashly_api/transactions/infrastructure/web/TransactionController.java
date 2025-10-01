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
        try {
            TransactionResponse response = createTransactionUseCase.execute(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable String id) {
        try {
            TransactionResponse response = getTransactionByIdUseCase.execute(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getTransactionsByUser(@RequestParam String userId) {
        try {
            List<TransactionResponse> responses = getTransactionsByUserUseCase.execute(userId);
            return ResponseEntity.ok(responses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAccount(@PathVariable String accountId) {
        try {
            List<TransactionResponse> responses = getTransactionsByAccountUseCase.execute(accountId);
            return ResponseEntity.ok(responses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransactionStatus(@PathVariable String id,
                                                                       @RequestBody UpdateTransactionRequest request) {
        try {
            TransactionResponse response = updateTransactionStatusUseCase.execute(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<TransactionResponse> cancelTransaction(@PathVariable String id) {
        try {
            TransactionResponse response = cancelTransactionUseCase.execute(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
