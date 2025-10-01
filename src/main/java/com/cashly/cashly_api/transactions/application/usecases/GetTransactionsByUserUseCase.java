package com.cashly.cashly_api.transactions.application.usecases;

import com.cashly.cashly_api.transactions.application.dto.TransactionResponse;
import com.cashly.cashly_api.transactions.application.ports.TransactionRepository;
import com.cashly.cashly_api.transactions.domain.entities.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetTransactionsByUserUseCase {

    private final TransactionRepository transactionRepository;

    public GetTransactionsByUserUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionResponse> execute(String userId) {
        validateUserId(userId);

        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        return transactions.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    private void validateUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return new TransactionResponse(
            transaction.getId().getValue().toString(),
            transaction.getUserId(),
            transaction.getType().name(),
            transaction.getStatus().name(),
            transaction.getAmount().getValue(),
            transaction.getCurrency(),
            transaction.getDescription().getValue(),
            transaction.getTransactionDate().getValue(),
            transaction.getSourceAccountId(),
            transaction.getDestinationAccountId(),
            transaction.getExpenseId(),
            transaction.getIncomeId(),
            transaction.getCreatedAt(),
            transaction.getUpdatedAt()
        );
    }
}
