package com.cashly.cashly_api.expenses.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cashly.cashly_api.expenses.application.ports.ExpenseRepository;
import com.cashly.cashly_api.expenses.domain.services.ExpenseService;

@Configuration
public class ExpenseConfig {

    @Bean
    public ExpenseService expenseService(ExpenseRepository expenseRepository) {
        return new ExpenseService(expenseRepository);
    }
}