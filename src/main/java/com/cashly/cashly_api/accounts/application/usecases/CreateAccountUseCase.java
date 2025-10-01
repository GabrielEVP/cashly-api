package com.cashly.cashly_api.accounts.application.usecases;

import com.cashly.cashly_api.accounts.application.dto.CreateAccountRequest;
import com.cashly.cashly_api.accounts.application.dto.AccountResponse;
import com.cashly.cashly_api.accounts.application.ports.AccountRepository;
import com.cashly.cashly_api.accounts.domain.entities.Account;
import com.cashly.cashly_api.accounts.domain.valueobjects.*;
import org.springframework.stereotype.Service;

@Service
public class CreateAccountUseCase {

    private final AccountRepository accountRepository;

    public CreateAccountUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountResponse execute(CreateAccountRequest request) {
        validateRequest(request);

        AccountId id = AccountId.generate();
        AccountName name = new AccountName(request.getName());
        AccountType type = new AccountType(request.getType());
        Balance balance = new Balance(request.getBalance());
        Currency currency = new Currency(request.getCurrency());

        Account account = new Account(id, name, type, balance, currency, request.getUserId());

        Account savedAccount = accountRepository.save(account);

        return mapToResponse(savedAccount);
    }

    private void validateRequest(CreateAccountRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Create account request cannot be null");
        }
        if (request.getName() == null) {
            throw new IllegalArgumentException("Account name cannot be null");
        }
        if (request.getType() == null) {
            throw new IllegalArgumentException("Account type cannot be null");
        }
        if (request.getBalance() == null) {
            throw new IllegalArgumentException("Balance cannot be null");
        }
        if (request.getCurrency() == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        }
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
    }

    private AccountResponse mapToResponse(Account account) {
        return new AccountResponse(
            account.getId().getValue().toString(),
            account.getName().getValue(),
            account.getType().getValue(),
            account.getBalance().getValue(),
            account.getCurrency().getValue(),
            account.isActive(),
            account.getUserId(),
            account.getCreatedAt(),
            account.getUpdatedAt()
        );
    }
}
