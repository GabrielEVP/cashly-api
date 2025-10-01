package com.cashly.cashly_api.transactions.application.usecases;

import com.cashly.cashly_api.transactions.application.dto.TransactionResponse;
import com.cashly.cashly_api.transactions.application.ports.TransactionRepository;
import com.cashly.cashly_api.transactions.domain.entities.Transaction;
import com.cashly.cashly_api.transactions.domain.services.TransactionService;
import com.cashly.cashly_api.transactions.domain.valueobjects.TransactionId;
import org.springframework.stereotype.Service;

@Service
public class CancelTransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;

    public CancelTransactionUseCase(TransactionRepository transactionRepository,
                                   TransactionService transactionService) {
        this.transactionRepository = transactionRepository;
        this.transactionService = transactionService;
    }

    public TransactionResponse execute(String id) {
        validateId(id);

        TransactionId transactionId = TransactionId.from(id);
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));

        if (!transactionService.canTransactionBeCancelled(transaction)) {
            throw new IllegalStateException("Transaction cannot be cancelled in its current state");
        }

        transaction.cancel();

        Transaction cancelledTransaction = transactionRepository.save(transaction);

        return mapToResponse(cancelledTransaction);
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
