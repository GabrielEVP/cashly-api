package com.cashly.cashly_api.incomes.application.ports;

import com.cashly.cashly_api.incomes.domain.entities.Income;
import com.cashly.cashly_api.incomes.domain.valueobjects.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class IncomeRepositoryUnitTest {
    
    @Test
    public void should_HaveSaveMethod_When_InterfaceIsCreated() {
        // Arrange & Act
        Class<IncomeRepository> repositoryClass = IncomeRepository.class;
        
        // Assert
        assertDoesNotThrow(() -> {
            Method saveMethod = repositoryClass.getMethod("save", Income.class);
            assertEquals(Income.class, saveMethod.getReturnType());
        });
    }
    
    @Test
    public void should_HaveFindByIdMethod_When_InterfaceIsCreated() {
        // Arrange & Act
        Class<IncomeRepository> repositoryClass = IncomeRepository.class;
        
        // Assert
        assertDoesNotThrow(() -> {
            Method findByIdMethod = repositoryClass.getMethod("findById", IncomeId.class);
            assertEquals(Optional.class, findByIdMethod.getReturnType());
        });
    }
    
    @Test
    public void should_HaveFindByUserIdMethod_When_InterfaceIsCreated() {
        // Arrange & Act
        Class<IncomeRepository> repositoryClass = IncomeRepository.class;
        
        // Assert
        assertDoesNotThrow(() -> {
            Method findByUserIdMethod = repositoryClass.getMethod("findByUserId", String.class);
            assertEquals(List.class, findByUserIdMethod.getReturnType());
        });
    }
    
    @Test
    public void should_HaveFindByUserIdAndDateRangeMethod_When_InterfaceIsCreated() {
        // Arrange & Act
        Class<IncomeRepository> repositoryClass = IncomeRepository.class;
        
        // Assert
        assertDoesNotThrow(() -> {
            Method findByUserIdAndDateRangeMethod = repositoryClass.getMethod(
                "findByUserIdAndDateRange", String.class, LocalDateTime.class, LocalDateTime.class);
            assertEquals(List.class, findByUserIdAndDateRangeMethod.getReturnType());
        });
    }
    
    @Test
    public void should_HaveFindByUserIdAndCategoryMethod_When_InterfaceIsCreated() {
        // Arrange & Act
        Class<IncomeRepository> repositoryClass = IncomeRepository.class;
        
        // Assert
        assertDoesNotThrow(() -> {
            Method findByUserIdAndCategoryMethod = repositoryClass.getMethod(
                "findByUserIdAndCategory", String.class, Category.class);
            assertEquals(List.class, findByUserIdAndCategoryMethod.getReturnType());
        });
    }
    
    @Test
    public void should_HaveDeleteByIdMethod_When_InterfaceIsCreated() {
        // Arrange & Act
        Class<IncomeRepository> repositoryClass = IncomeRepository.class;
        
        // Assert
        assertDoesNotThrow(() -> {
            Method deleteByIdMethod = repositoryClass.getMethod("deleteById", IncomeId.class);
            assertEquals(void.class, deleteByIdMethod.getReturnType());
        });
    }
    
    @Test
    public void should_HaveExistsByIdMethod_When_InterfaceIsCreated() {
        // Arrange & Act
        Class<IncomeRepository> repositoryClass = IncomeRepository.class;
        
        // Assert
        assertDoesNotThrow(() -> {
            Method existsByIdMethod = repositoryClass.getMethod("existsById", IncomeId.class);
            assertEquals(boolean.class, existsByIdMethod.getReturnType());
        });
    }
    
    @Test
    public void should_BeAnInterface_When_InterfaceIsCreated() {
        // Arrange & Act & Assert
        assertTrue(IncomeRepository.class.isInterface());
    }
}