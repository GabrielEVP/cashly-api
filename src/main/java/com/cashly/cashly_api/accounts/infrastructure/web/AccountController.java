package com.cashly.cashly_api.accounts.infrastructure.web;

import com.cashly.cashly_api.accounts.application.dto.CreateAccountRequest;
import com.cashly.cashly_api.accounts.application.dto.UpdateAccountRequest;
import com.cashly.cashly_api.accounts.application.dto.AccountResponse;
import com.cashly.cashly_api.accounts.application.usecases.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountByIdUseCase getAccountByIdUseCase;
    private final GetAccountsByUserUseCase getAccountsByUserUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;
    private final DeactivateAccountUseCase deactivateAccountUseCase;

    public AccountController(CreateAccountUseCase createAccountUseCase,
                           GetAccountByIdUseCase getAccountByIdUseCase,
                           GetAccountsByUserUseCase getAccountsByUserUseCase,
                           UpdateAccountUseCase updateAccountUseCase,
                           DeleteAccountUseCase deleteAccountUseCase,
                           DeactivateAccountUseCase deactivateAccountUseCase) {
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountByIdUseCase = getAccountByIdUseCase;
        this.getAccountsByUserUseCase = getAccountsByUserUseCase;
        this.updateAccountUseCase = updateAccountUseCase;
        this.deleteAccountUseCase = deleteAccountUseCase;
        this.deactivateAccountUseCase = deactivateAccountUseCase;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody CreateAccountRequest request) {
        AccountResponse response = createAccountUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(
            @PathVariable String id,
            @RequestHeader("X-User-Id") String userId) {
        AccountResponse response = getAccountByIdUseCase.execute(id, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAccountsByUser(
            @RequestHeader("X-User-Id") String userId) {
        List<AccountResponse> responses = getAccountsByUserUseCase.execute(userId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable String id,
            @RequestBody UpdateAccountRequest request,
            @RequestHeader("X-User-Id") String userId) {
        AccountResponse response = updateAccountUseCase.execute(id, request, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable String id,
            @RequestHeader("X-User-Id") String userId) {
        deleteAccountUseCase.execute(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<AccountResponse> deactivateAccount(
            @PathVariable String id,
            @RequestHeader("X-User-Id") String userId) {
        AccountResponse response = deactivateAccountUseCase.execute(id, userId);
        return ResponseEntity.ok(response);
    }
}
