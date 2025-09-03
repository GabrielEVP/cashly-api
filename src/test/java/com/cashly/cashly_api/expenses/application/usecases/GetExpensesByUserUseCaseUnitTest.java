package com.cashly.cashly_api.expenses.application.usecases;

import com.cashly.cashly_api.expenses.application.dto.ExpenseResponse;
import com.cashly.cashly_api.expenses.application.ports.ExpenseRepository;
import com.cashly.cashly_api.expenses.domain.entities.Expense;
import com.cashly.cashly_api.expenses.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class GetExpensesByUserUseCaseUnitTest {
    
    @Mock
    private ExpenseRepository expenseRepository;
    
    private GetExpensesByUserUseCase getExpensesByUserUseCase;
    private String validUserId;
    private List<Expense> testExpenses;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        getExpensesByUserUseCase = new GetExpensesByUserUseCase(expenseRepository);
        
        validUserId = "user123";
        testExpenses = Arrays.asList(
            new Expense(
                ExpenseId.generate(),
                new Amount(new BigDecimal("500.00")),
                new Description("Grocery shopping"),
                new Category("FOOD_DINING"),
                LocalDate.of(2024, 1, 15),
                validUserId
            ),
            new Expense(
                ExpenseId.generate(),
                new Amount(new BigDecimal("1200.00")),
                new Description("Monthly rent"),
                new Category("HOUSING"),
                LocalDate.of(2024, 1, 1),
                validUserId
            ),
            new Expense(
                ExpenseId.generate(),
                new Amount(new BigDecimal("300.00")),
                new Description("Electricity bill"),
                new Category("OTHER"),
                LocalDate.of(2024, 1, 10),
                validUserId
            )
        );
    }
    
    @Test
    void should_ReturnListOfExpenseResponses_When_ValidUserIdProvided() {
        when(expenseRepository.findByUserId(validUserId)).thenReturn(testExpenses);
        
        List<ExpenseResponse> responses = getExpensesByUserUseCase.execute(validUserId);
        
        assertNotNull(responses);
        assertEquals(3, responses.size());
        
        ExpenseResponse firstResponse = responses.get(0);
        assertEquals(testExpenses.get(0).getId().getValue().toString(), firstResponse.getId());
        assertEquals(testExpenses.get(0).getAmount().getValue(), firstResponse.getAmount());
        assertEquals(testExpenses.get(0).getDescription().getValue(), firstResponse.getDescription());
        assertEquals(testExpenses.get(0).getCategory().getValue(), firstResponse.getCategory());
        assertEquals(testExpenses.get(0).getUserId(), firstResponse.getUserId());
        
        verify(expenseRepository, times(1)).findByUserId(validUserId);
    }
    
    @Test
    void should_ReturnEmptyList_When_UserHasNoExpenses() {
        when(expenseRepository.findByUserId(validUserId)).thenReturn(Collections.emptyList());
        
        List<ExpenseResponse> responses = getExpensesByUserUseCase.execute(validUserId);
        
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
        
        verify(expenseRepository, times(1)).findByUserId(validUserId);
    }
    
    @Test
    void should_ReturnSingleExpenseResponse_When_UserHasOneExpense() {
        Expense singleExpense = new Expense(
            ExpenseId.generate(),
            new Amount(new BigDecimal("250.00")),
            new Description("Coffee"),
            new Category("FOOD_DINING"),
            LocalDate.now(),
            validUserId
        );
        
        when(expenseRepository.findByUserId(validUserId)).thenReturn(Collections.singletonList(singleExpense));
        
        List<ExpenseResponse> responses = getExpensesByUserUseCase.execute(validUserId);
        
        assertNotNull(responses);
        assertEquals(1, responses.size());
        
        ExpenseResponse response = responses.get(0);
        assertEquals(singleExpense.getId().getValue().toString(), response.getId());
        assertEquals(singleExpense.getAmount().getValue(), response.getAmount());
        assertEquals(singleExpense.getDescription().getValue(), response.getDescription());
        assertEquals(singleExpense.getCategory().getValue(), response.getCategory());
        assertEquals(singleExpense.getUserId(), response.getUserId());
        
        verify(expenseRepository, times(1)).findByUserId(validUserId);
    }
    
    @Test
    void should_ThrowException_When_UserIdIsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> getExpensesByUserUseCase.execute(null)
        );
        
        assertEquals("User ID cannot be null", exception.getMessage());
        verify(expenseRepository, never()).findByUserId(anyString());
    }
    
    @Test
    void should_ThrowException_When_UserIdIsEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> getExpensesByUserUseCase.execute("")
        );
        
        assertEquals("User ID cannot be empty or blank", exception.getMessage());
        verify(expenseRepository, never()).findByUserId(anyString());
    }
    
    @Test
    void should_ThrowException_When_UserIdIsWhitespace() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> getExpensesByUserUseCase.execute("   ")
        );
        
        assertEquals("User ID cannot be empty or blank", exception.getMessage());
        verify(expenseRepository, never()).findByUserId(anyString());
    }
    
    @Test
    void should_ThrowException_When_UserIdIsTab() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> getExpensesByUserUseCase.execute("\t")
        );
        
        assertEquals("User ID cannot be empty or blank", exception.getMessage());
        verify(expenseRepository, never()).findByUserId(anyString());
    }
    
    @Test
    void should_ThrowException_When_UserIdIsNewline() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> getExpensesByUserUseCase.execute("\n")
        );
        
        assertEquals("User ID cannot be empty or blank", exception.getMessage());
        verify(expenseRepository, never()).findByUserId(anyString());
    }
    
    @Test
    void should_HandleRepositoryException_When_FindByUserIdFails() {
        when(expenseRepository.findByUserId(validUserId))
            .thenThrow(new RuntimeException("Database connection error"));
        
        RuntimeException exception = assertThrows(
            RuntimeException.class, 
            () -> getExpensesByUserUseCase.execute(validUserId)
        );
        
        assertEquals("Database connection error", exception.getMessage());
        verify(expenseRepository, times(1)).findByUserId(validUserId);
    }
    
    @Test
    void should_ReturnCorrectResponsesForExpensesWithZeroAmounts_When_UserHasZeroAmountExpenses() {
        List<Expense> zeroAmountExpenses = Arrays.asList(
            new Expense(
                ExpenseId.generate(),
                new Amount(BigDecimal.ZERO),
                new Description("Free sample"),
                new Category("OTHER"),
                LocalDate.now(),
                validUserId
            ),
            new Expense(
                ExpenseId.generate(),
                new Amount(new BigDecimal("100.00")),
                new Description("Regular expense"),
                new Category("FOOD_DINING"),
                LocalDate.now(),
                validUserId
            )
        );
        
        when(expenseRepository.findByUserId(validUserId)).thenReturn(zeroAmountExpenses);
        
        List<ExpenseResponse> responses = getExpensesByUserUseCase.execute(validUserId);
        
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(BigDecimal.ZERO, responses.get(0).getAmount());
        assertEquals(new BigDecimal("100.00"), responses.get(1).getAmount());
        
        verify(expenseRepository, times(1)).findByUserId(validUserId);
    }
    
    @Test
    void should_ReturnCorrectResponsesForExpensesWithLargeAmounts_When_UserHasLargeAmountExpenses() {
        BigDecimal largeAmount = new BigDecimal("999999999.99");
        List<Expense> largeAmountExpenses = Collections.singletonList(
            new Expense(
                ExpenseId.generate(),
                new Amount(largeAmount),
                new Description("Investment expense"),
                new Category("SHOPPING"),
                LocalDate.now(),
                validUserId
            )
        );
        
        when(expenseRepository.findByUserId(validUserId)).thenReturn(largeAmountExpenses);
        
        List<ExpenseResponse> responses = getExpensesByUserUseCase.execute(validUserId);
        
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(largeAmount, responses.get(0).getAmount());
        
        verify(expenseRepository, times(1)).findByUserId(validUserId);
    }
    
    @Test
    void should_ReturnCorrectResponsesForExpensesWithEmptyDescriptions_When_UserHasEmptyDescriptionExpenses() {
        List<Expense> emptyDescriptionExpenses = Collections.singletonList(
            new Expense(
                ExpenseId.generate(),
                new Amount(new BigDecimal("50.00")),
                new Description(""),
                new Category("OTHER"),
                LocalDate.now(),
                validUserId
            )
        );
        
        when(expenseRepository.findByUserId(validUserId)).thenReturn(emptyDescriptionExpenses);
        
        List<ExpenseResponse> responses = getExpensesByUserUseCase.execute(validUserId);
        
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("", responses.get(0).getDescription());
        
        verify(expenseRepository, times(1)).findByUserId(validUserId);
    }
    
    @Test
    void should_ReturnCorrectResponsesForExpensesWithEmptyCategories_When_UserHasEmptyCategoryExpenses() {
        List<Expense> emptyCategoryExpenses = Collections.singletonList(
            new Expense(
                ExpenseId.generate(),
                new Amount(new BigDecimal("75.00")),
                new Description("No category expense"),
                new Category(""),
                LocalDate.now(),
                validUserId
            )
        );
        
        when(expenseRepository.findByUserId(validUserId)).thenReturn(emptyCategoryExpenses);
        
        List<ExpenseResponse> responses = getExpensesByUserUseCase.execute(validUserId);
        
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("", responses.get(0).getCategory());
        
        verify(expenseRepository, times(1)).findByUserId(validUserId);
    }
    
    @Test
    void should_HandleLargeListOfExpenses_When_UserHasManyExpenses() {
        List<Expense> manyExpenses = Arrays.asList(
            new Expense(ExpenseId.generate(), new Amount(new BigDecimal("100.00")), new Description("Expense 1"), new Category("FOOD_DINING"), LocalDate.now(), validUserId),
            new Expense(ExpenseId.generate(), new Amount(new BigDecimal("200.00")), new Description("Expense 2"), new Category("TRANSPORTATION"), LocalDate.now(), validUserId),
            new Expense(ExpenseId.generate(), new Amount(new BigDecimal("300.00")), new Description("Expense 3"), new Category("OTHER"), LocalDate.now(), validUserId),
            new Expense(ExpenseId.generate(), new Amount(new BigDecimal("400.00")), new Description("Expense 4"), new Category("HOUSING"), LocalDate.now(), validUserId),
            new Expense(ExpenseId.generate(), new Amount(new BigDecimal("500.00")), new Description("Expense 5"), new Category("ENTERTAINMENT"), LocalDate.now(), validUserId)
        );
        
        when(expenseRepository.findByUserId(validUserId)).thenReturn(manyExpenses);
        
        List<ExpenseResponse> responses = getExpensesByUserUseCase.execute(validUserId);
        
        assertNotNull(responses);
        assertEquals(5, responses.size());
        
        for (int i = 0; i < responses.size(); i++) {
            assertEquals(manyExpenses.get(i).getId().getValue().toString(), responses.get(i).getId());
            assertEquals(manyExpenses.get(i).getAmount().getValue(), responses.get(i).getAmount());
            assertEquals(manyExpenses.get(i).getDescription().getValue(), responses.get(i).getDescription());
            assertEquals(manyExpenses.get(i).getCategory().getValue(), responses.get(i).getCategory());
            assertEquals(validUserId, responses.get(i).getUserId());
        }
        
        verify(expenseRepository, times(1)).findByUserId(validUserId);
    }
    
    @Test
    void should_PassCorrectUserId_When_CallingRepository() {
        when(expenseRepository.findByUserId(validUserId)).thenReturn(testExpenses);
        
        getExpensesByUserUseCase.execute(validUserId);
        
        verify(expenseRepository, times(1)).findByUserId(eq(validUserId));
    }
}