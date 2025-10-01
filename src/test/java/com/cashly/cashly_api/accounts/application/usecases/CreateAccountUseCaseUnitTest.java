package com.cashly.cashly_api.accounts.application.usecases;

import com.cashly.cashly_api.accounts.application.dto.CreateAccountRequest;
import com.cashly.cashly_api.accounts.application.dto.AccountResponse;
import com.cashly.cashly_api.accounts.application.ports.AccountRepository;
import com.cashly.cashly_api.accounts.domain.entities.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateAccountUseCaseUnitTest {

    private AccountRepository accountRepository;
    private CreateAccountUseCase createAccountUseCase;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        createAccountUseCase = new CreateAccountUseCase(accountRepository);
    }

    @Test
    void should_CreateAccount_When_ValidRequestProvided() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest(
            "Main Checking",
            "CHECKING",
            new BigDecimal("1000.00"),
            "USD",
            "user123"
        );

        when(accountRepository.save(any(Account.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        AccountResponse response = createAccountUseCase.execute(request);

        // Assert
        assertNotNull(response);
        assertEquals("Main Checking", response.getName());
        assertEquals("CHECKING", response.getType());
        assertEquals(0, new BigDecimal("1000.00").compareTo(response.getBalance()));
        assertEquals("USD", response.getCurrency());
        assertEquals("user123", response.getUserId());
        assertTrue(response.isActive());

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountCaptor.capture());

        Account savedAccount = accountCaptor.getValue();
        assertEquals("Main Checking", savedAccount.getName().getValue());
    }

    @Test
    void should_ThrowException_When_NullRequestProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> createAccountUseCase.execute(null)
        );
        assertEquals("Create account request cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullNameProvided() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest(
            null,
            "CHECKING",
            new BigDecimal("1000.00"),
            "USD",
            "user123"
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> createAccountUseCase.execute(request)
        );
        assertEquals("Account name cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullTypeProvided() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest(
            "Test Account",
            null,
            new BigDecimal("1000.00"),
            "USD",
            "user123"
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> createAccountUseCase.execute(request)
        );
        assertEquals("Account type cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullBalanceProvided() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest(
            "Test Account",
            "CHECKING",
            null,
            "USD",
            "user123"
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> createAccountUseCase.execute(request)
        );
        assertEquals("Balance cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullCurrencyProvided() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest(
            "Test Account",
            "CHECKING",
            new BigDecimal("1000.00"),
            null,
            "user123"
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> createAccountUseCase.execute(request)
        );
        assertEquals("Currency cannot be null", exception.getMessage());
    }

    @Test
    void should_ThrowException_When_NullUserIdProvided() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest(
            "Test Account",
            "CHECKING",
            new BigDecimal("1000.00"),
            "USD",
            null
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> createAccountUseCase.execute(request)
        );
        assertEquals("User ID cannot be null", exception.getMessage());
    }
}
