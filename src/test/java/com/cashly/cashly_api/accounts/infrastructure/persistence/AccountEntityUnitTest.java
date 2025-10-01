package com.cashly.cashly_api.accounts.infrastructure.persistence;

import com.cashly.cashly_api.accounts.domain.entities.Account;
import com.cashly.cashly_api.accounts.domain.valueobjects.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountEntityUnitTest {

    @Test
    void should_CreateEntityFromDomain_When_ValidAccountProvided() {
        // Arrange
        Account account = createTestAccount();

        // Act
        AccountEntity entity = AccountEntity.fromDomain(account);

        // Assert
        assertNotNull(entity);
        assertEquals(account.getId().getValue().toString(), entity.getId());
        assertEquals(account.getName().getValue(), entity.getName());
        assertEquals(account.getType().getValue(), entity.getAccountType());
        assertEquals(0, account.getBalance().getValue().compareTo(entity.getBalance()));
        assertEquals(account.getCurrency().getValue(), entity.getCurrency());
        assertEquals(account.isActive(), entity.isActive());
        assertEquals(account.getUserId(), entity.getUserId());
    }

    @Test
    void should_ThrowException_When_NullAccountProvidedToFromDomain() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> AccountEntity.fromDomain(null)
        );
        assertEquals("Account cannot be null", exception.getMessage());
    }

    @Test
    void should_UpdateEntityFromDomain_When_ValidAccountProvided() {
        // Arrange
        Account account = createTestAccount();
        AccountEntity entity = AccountEntity.fromDomain(account);

        Account updatedAccount = createTestAccount();
        updatedAccount.updateName(new AccountName("Updated Account"));
        updatedAccount.updateBalance(new Balance(new BigDecimal("5000.00")));

        // Act
        entity.updateFromDomain(updatedAccount);

        // Assert
        assertEquals("Updated Account", entity.getName());
        assertEquals(0, new BigDecimal("5000.00").compareTo(entity.getBalance()));
    }

    @Test
    void should_ThrowException_When_NullAccountProvidedToUpdateFromDomain() {
        // Arrange
        AccountEntity entity = new AccountEntity();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> entity.updateFromDomain(null)
        );
        assertEquals("Account cannot be null", exception.getMessage());
    }

    @Test
    void should_ConvertToDomain_When_ValidEntityProvided() {
        // Arrange
        Account originalAccount = createTestAccount();
        AccountEntity entity = AccountEntity.fromDomain(originalAccount);

        // Act
        Account domainAccount = entity.toDomain();

        // Assert
        assertNotNull(domainAccount);
        assertEquals(entity.getId(), domainAccount.getId().getValue().toString());
        assertEquals(entity.getName(), domainAccount.getName().getValue());
        assertEquals(entity.getAccountType(), domainAccount.getType().getValue());
        assertEquals(0, entity.getBalance().compareTo(domainAccount.getBalance().getValue()));
        assertEquals(entity.getCurrency(), domainAccount.getCurrency().getValue());
        assertEquals(entity.isActive(), domainAccount.isActive());
        assertEquals(entity.getUserId(), domainAccount.getUserId());
    }

    private Account createTestAccount() {
        return new Account(
            AccountId.generate(),
            new AccountName("Test Account"),
            new AccountType("CHECKING"),
            new Balance(new BigDecimal("1000.00")),
            new Currency("USD"),
            "user123"
        );
    }
}
