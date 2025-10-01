package com.cashly.cashly_api.transactions.application.usecases;

import com.cashly.cashly_api.transactions.application.dto.TransactionResponse;
import com.cashly.cashly_api.transactions.application.ports.TransactionRepository;
import com.cashly.cashly_api.transactions.domain.entities.Transaction;
import com.cashly.cashly_api.transactions.domain.valueobjects.TransactionId;
import org.springframework.stereotype.Service;

@Service
public class GetTransactionByIdUseCase {

    private final TransactionRepository transactionRepository;

    public GetTransactionByIdUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionResponse execute(String id) {
        validateId(id);

        TransactionId transactionId = TransactionId.from(id);
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));

        return mapToResponse(transaction);
    }

    private void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction ID cannot be null or empty");
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
