package com.cashly.cashly_api.accounts.application.usecases;

import com.cashly.cashly_api.accounts.application.dto.AccountResponse;
import com.cashly.cashly_api.accounts.application.ports.AccountRepository;
import com.cashly.cashly_api.accounts.domain.entities.Account;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetAccountsByUserUseCase {

    private final AccountRepository accountRepository;

    public GetAccountsByUserUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<AccountResponse> execute(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        List<Account> accounts = accountRepository.findByUserId(userId);

        return accounts.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
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
