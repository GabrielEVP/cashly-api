package com.cashly.cashly_api.accounts.application.usecases;

import com.cashly.cashly_api.accounts.application.dto.AccountResponse;
import com.cashly.cashly_api.accounts.application.ports.AccountRepository;
import com.cashly.cashly_api.accounts.domain.entities.Account;
import com.cashly.cashly_api.accounts.domain.valueobjects.AccountId;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetAccountByIdUseCase {

    private final AccountRepository accountRepository;

    public GetAccountByIdUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountResponse execute(String accountId, String userId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        AccountId id = new AccountId(UUID.fromString(accountId));
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (!account.belongsToUser(userId)) {
            throw new IllegalArgumentException("Account does not belong to user");
        }

        return mapToResponse(account);
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
