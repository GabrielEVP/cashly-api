package com.cashly.cashly_api.accounts.infrastructure.persistence;

import com.cashly.cashly_api.accounts.domain.entities.Account;
import com.cashly.cashly_api.accounts.domain.valueobjects.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "accounts", indexes = {
    @Index(name = "idx_account_user_id", columnList = "user_id"),
    @Index(name = "idx_account_user_type", columnList = "user_id, account_type"),
    @Index(name = "idx_account_user_active", columnList = "user_id, is_active")
})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class AccountEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "account_type", length = 20, nullable = false)
    private String accountType;

    @Column(name = "balance", precision = 19, scale = 2, nullable = false)
    private BigDecimal balance;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "user_id", length = 36, nullable = false)
    private String userId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static AccountEntity fromDomain(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }

        AccountEntity entity = new AccountEntity();
        entity.id = account.getId().getValue().toString();
        entity.name = account.getName().getValue();
        entity.accountType = account.getType().getValue();
        entity.balance = account.getBalance().getValue();
        entity.currency = account.getCurrency().getValue();
        entity.active = account.isActive();
        entity.userId = account.getUserId();
        entity.createdAt = account.getCreatedAt();
        entity.updatedAt = account.getUpdatedAt();

        return entity;
    }

    public void updateFromDomain(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }

        this.name = account.getName().getValue();
        this.balance = account.getBalance().getValue();
        this.active = account.isActive();
        this.updatedAt = account.getUpdatedAt();
    }

    public Account toDomain() {
        AccountId accountId = new AccountId(UUID.fromString(this.id));
        AccountName accountName = new AccountName(this.name);
        AccountType accountType = new AccountType(this.accountType);
        Balance domainBalance = new Balance(this.balance);
        Currency domainCurrency = new Currency(this.currency);

        Account account = new Account(accountId, accountName, accountType,
            domainBalance, domainCurrency, this.userId);

        try {
            java.lang.reflect.Field activeField = Account.class.getDeclaredField("active");
            java.lang.reflect.Field createdAtField = Account.class.getDeclaredField("createdAt");
            java.lang.reflect.Field updatedAtField = Account.class.getDeclaredField("updatedAt");

            activeField.setAccessible(true);
            createdAtField.setAccessible(true);
            updatedAtField.setAccessible(true);

            activeField.set(account, this.active);
            createdAtField.set(account, this.createdAt);
            updatedAtField.set(account, this.updatedAt);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set fields on domain entity", e);
        }

        return account;
    }
}
