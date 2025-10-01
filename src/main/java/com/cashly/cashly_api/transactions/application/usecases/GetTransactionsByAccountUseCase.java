package com.cashly.cashly_api.transactions.application.usecases;

import com.cashly.cashly_api.transactions.application.dto.TransactionResponse;
import com.cashly.cashly_api.transactions.application.ports.TransactionRepository;
import com.cashly.cashly_api.transactions.domain.entities.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetTransactionsByAccountUseCase {

    private final TransactionRepository transactionRepository;

    public GetTransactionsByAccountUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionResponse> execute(String accountId) {
        validateAccountId(accountId);

        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);

        return transactions.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    private void validateAccountId(String accountId) {
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
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
