package com.cashly.cashly_api.incomes.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cashly.cashly_api.incomes.application.ports.IncomeRepository;
import com.cashly.cashly_api.incomes.domain.services.IncomeService;

@Configuration
public class IncomeConfig {

    @Bean
    public IncomeService incomeService(IncomeRepository incomeRepository) {
        return new IncomeService(incomeRepository);
    }
}