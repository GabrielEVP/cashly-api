package com.cashly.cashly_api.transactions.infrastructure.config;

import com.cashly.cashly_api.transactions.domain.services.TransactionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionConfig {

    @Bean
    public TransactionService transactionService() {
        return new TransactionService();
    }
}
