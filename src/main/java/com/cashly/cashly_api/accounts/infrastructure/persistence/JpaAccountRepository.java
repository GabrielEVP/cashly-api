package com.cashly.cashly_api.accounts.infrastructure.persistence;

import com.cashly.cashly_api.accounts.application.ports.AccountRepository;
import com.cashly.cashly_api.accounts.domain.entities.Account;
import com.cashly.cashly_api.accounts.domain.valueobjects.AccountId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JpaAccountRepository implements AccountRepository {

    private final SpringDataAccountRepository springDataAccountRepository;

    public JpaAccountRepository(SpringDataAccountRepository springDataAccountRepository) {
        this.springDataAccountRepository = springDataAccountRepository;
    }

    @Override
    public Account save(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }

        String accountIdStr = account.getId().getValue().toString();
        Optional<AccountEntity> existingEntity = springDataAccountRepository.findById(accountIdStr);

        AccountEntity entity;
        if (existingEntity.isPresent()) {
            entity = existingEntity.get();
            entity.updateFromDomain(account);
        } else {
            entity = AccountEntity.fromDomain(account);
        }

        AccountEntity savedEntity = springDataAccountRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Account> findById(AccountId id) {
        if (id == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }

        return springDataAccountRepository.findById(id.getValue().toString())
            .map(AccountEntity::toDomain);
    }

    @Override
    public List<Account> findByUserId(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        return springDataAccountRepository.findByUserId(userId).stream()
            .map(AccountEntity::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteById(AccountId id) {
        if (id == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }

        springDataAccountRepository.deleteById(id.getValue().toString());
    }

    @Override
    public boolean existsById(AccountId id) {
        if (id == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }

        return springDataAccountRepository.existsById(id.getValue().toString());
    }
}
