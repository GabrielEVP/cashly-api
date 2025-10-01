package com.cashly.cashly_api.accounts.domain.entities;

import com.cashly.cashly_api.accounts.domain.valueobjects.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountUnitTest {

    @Test
    void should_CreateAccount_When_ValidParametersProvided() {
        // Arrange
        AccountId id = AccountId.generate();
        AccountName name = new AccountName("Main Checking");
        AccountType type = new AccountType("CHECKING");
        Balance balance = new Balance(new BigDecimal("1000.00"));
        Currency currency = new Currency("USD");
        String userId = "user123";

        // Act
        Account account = new Account(id, name, type, balance, currency, userId);

        // Assert
        assertNotNull(account);
        assertEquals(id, account.getId());
        assertEquals(name, account.getName());
        assertEquals(type, account.getType());
        assertEquals(balance, account.getBalance());
        assertEquals(currency, account.getCurrency());
        assertEquals(userId, account.getUserId());
        assertTrue(account.isActive());
        assertNotNull(account.getCreatedAt());
        assertNotNull(account.getUpdatedAt());
    }

    @Test
    void should_TrimUserId_When_UserIdHasWhitespace() {
        // Arrange
        AccountId id = AccountId.generate();
        AccountName name = new AccountName("Test Account");
        AccountType type = new AccountType("SAVINGS");
        Balance balance = new Balance(BigDecimal.ZERO);
        Currency currency = new Currency("EUR");

        // Act
        Account account = new Account(id, name, type, balance, currency, "  user123  ");

        // Assert
        assertEquals("user123", account.getUserId());
    }

    @Test
    void should_ThrowException_When_NullAccountIdProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Account(null, new AccountName("Test"), new AccountType("CHECKING"),
                new Balance(BigDecimal.ZERO), new Currency("USD"), "user123")
        );
        assertEquals("Account ID cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullAccountNameProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Account(AccountId.generate(), null, new AccountType("CHECKING"),
                new Balance(BigDecimal.ZERO), new Currency("USD"), "user123")
        );
        assertEquals("Account name cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullAccountTypeProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Account(AccountId.generate(), new AccountName("Test"), null,
                new Balance(BigDecimal.ZERO), new Currency("USD"), "user123")
        );
        assertEquals("Account type cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullBalanceProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Account(AccountId.generate(), new AccountName("Test"),
                new AccountType("CHECKING"), null, new Currency("USD"), "user123")
        );
        assertEquals("Balance cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullCurrencyProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Account(AccountId.generate(), new AccountName("Test"),
                new AccountType("CHECKING"), new Balance(BigDecimal.ZERO), null, "user123")
        );
        assertEquals("Currency cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullUserIdProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Account(AccountId.generate(), new AccountName("Test"),
                new AccountType("CHECKING"), new Balance(BigDecimal.ZERO),
                new Currency("USD"), null)
        );
        assertEquals("User ID cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_EmptyUserIdProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Account(AccountId.generate(), new AccountName("Test"),
                new AccountType("CHECKING"), new Balance(BigDecimal.ZERO),
                new Currency("USD"), "   ")
        );
        assertEquals("User ID cannot be empty", exception.getMessage());
    }

    @Test
    void should_UpdateName_When_ValidNameProvided() {
        // Arrange
        Account account = createTestAccount();
        AccountName newName = new AccountName("Updated Account Name");

        // Act
        account.updateName(newName);

        // Assert
        assertEquals(newName, account.getName());
        assertNotNull(account.getUpdatedAt());
    }

    @Test
    void should_ThrowException_When_UpdatingWithNullName() {
        // Arrange
        Account account = createTestAccount();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> account.updateName(null)
        );
        assertEquals("Account name cannot be null", exception.getMessage());
    }

    @Test
    void should_UpdateBalance_When_ValidBalanceProvided() {
        // Arrange
        Account account = createTestAccount();
        Balance newBalance = new Balance(new BigDecimal("5000.00"));

        // Act
        account.updateBalance(newBalance);

        // Assert
        assertEquals(newBalance, account.getBalance());
        assertNotNull(account.getUpdatedAt());
    }

    @Test
    void should_ThrowException_When_UpdatingWithNullBalance() {
        // Arrange
        Account account = createTestAccount();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> account.updateBalance(null)
        );
        assertEquals("Balance cannot be null", exception.getMessage());
    }

    @Test
    void should_DeactivateAccount_When_DeactivateCalled() {
        // Arrange
        Account account = createTestAccount();

        // Act
        account.deactivate();

        // Assert
        assertFalse(account.isActive());
        assertNotNull(account.getUpdatedAt());
    }

    @Test
    void should_ActivateAccount_When_ActivateCalled() {
        // Arrange
        Account account = createTestAccount();
        account.deactivate();

        // Act
        account.activate();

        // Assert
        assertTrue(account.isActive());
        assertNotNull(account.getUpdatedAt());
    }

    @Test
    void should_ReturnTrue_When_AccountBelongsToUser() {
        // Arrange
        String userId = "user123";
        Account account = createTestAccount(userId);

        // Act & Assert
        assertTrue(account.belongsToUser(userId));
    }

    @Test
    void should_ReturnFalse_When_AccountDoesNotBelongToUser() {
        // Arrange
        Account account = createTestAccount("user123");

        // Act & Assert
        assertFalse(account.belongsToUser("user456"));
    }

    @Test
    void should_ReturnFalse_When_CheckingOwnershipWithNullUserId() {
        // Arrange
        Account account = createTestAccount("user123");

        // Act & Assert
        assertFalse(account.belongsToUser(null));
    }

    @Test
    void should_ReturnTrue_When_ActiveAccountCanAcceptTransactions() {
        // Arrange
        Account account = createTestAccount();

        // Act & Assert
        assertTrue(account.canAcceptTransactions());
    }

    @Test
    void should_ReturnFalse_When_InactiveAccountCannotAcceptTransactions() {
        // Arrange
        Account account = createTestAccount();
        account.deactivate();

        // Act & Assert
        assertFalse(account.canAcceptTransactions());
    }

    @Test
    void should_ReturnTrue_When_CheckingAccountIsDebitType() {
        // Arrange
        Account account = new Account(
            AccountId.generate(),
            new AccountName("Checking"),
            new AccountType("CHECKING"),
            new Balance(BigDecimal.ZERO),
            new Currency("USD"),
            "user123"
        );

        // Act & Assert
        assertTrue(account.isDebitAccount());
        assertFalse(account.isCreditAccount());
    }

    @Test
    void should_ReturnTrue_When_CreditCardAccountIsCreditType() {
        // Arrange
        Account account = new Account(
            AccountId.generate(),
            new AccountName("Credit Card"),
            new AccountType("CREDIT_CARD"),
            new Balance(new BigDecimal("-500.00")),
            new Currency("USD"),
            "user123"
        );

        // Act & Assert
        assertTrue(account.isCreditAccount());
        assertFalse(account.isDebitAccount());
    }

    @Test
    void should_BeEqual_When_SameAccountId() {
        // Arrange
        AccountId id = AccountId.generate();
        Account account1 = new Account(id, new AccountName("Account 1"),
            new AccountType("CHECKING"), new Balance(BigDecimal.ZERO),
            new Currency("USD"), "user123");
        Account account2 = new Account(id, new AccountName("Account 2"),
            new AccountType("SAVINGS"), new Balance(new BigDecimal("100.00")),
            new Currency("EUR"), "user456");

        // Act & Assert
        assertEquals(account1, account2);
        assertEquals(account1.hashCode(), account2.hashCode());
    }

    @Test
    void should_NotBeEqual_When_DifferentAccountId() {
        // Arrange
        Account account1 = createTestAccount();
        Account account2 = createTestAccount();

        // Act & Assert
        assertNotEquals(account1, account2);
    }

    @Test
    void should_ReturnStringRepresentation_When_ToStringCalled() {
        // Arrange
        Account account = createTestAccount();

        // Act
        String result = account.toString();

        // Assert
        assertTrue(result.contains("Account"));
        assertTrue(result.contains("user123"));
    }

    private Account createTestAccount() {
        return createTestAccount("user123");
    }

    private Account createTestAccount(String userId) {
        return new Account(
            AccountId.generate(),
            new AccountName("Test Account"),
            new AccountType("CHECKING"),
            new Balance(new BigDecimal("1000.00")),
            new Currency("USD"),
            userId
        );
    }
}
