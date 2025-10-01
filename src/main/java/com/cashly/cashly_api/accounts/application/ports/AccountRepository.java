package com.cashly.cashly_api.accounts.application.ports;

import com.cashly.cashly_api.accounts.domain.entities.Account;
import com.cashly.cashly_api.accounts.domain.valueobjects.AccountId;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> findById(AccountId id);
    List<Account> findByUserId(String userId);
    void deleteById(AccountId id);
    boolean existsById(AccountId id);
}
