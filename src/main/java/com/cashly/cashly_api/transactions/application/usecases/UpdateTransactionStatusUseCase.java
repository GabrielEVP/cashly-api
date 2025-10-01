package com.cashly.cashly_api.transactions.application.usecases;

import com.cashly.cashly_api.transactions.application.dto.TransactionResponse;
import com.cashly.cashly_api.transactions.application.dto.UpdateTransactionRequest;
import com.cashly.cashly_api.transactions.application.ports.TransactionRepository;
import com.cashly.cashly_api.transactions.domain.entities.Transaction;
import com.cashly.cashly_api.transactions.domain.valueobjects.Description;
import com.cashly.cashly_api.transactions.domain.valueobjects.TransactionId;
import com.cashly.cashly_api.transactions.domain.valueobjects.TransactionStatus;
import org.springframework.stereotype.Service;

@Service
public class UpdateTransactionStatusUseCase {

    private final TransactionRepository transactionRepository;

    public UpdateTransactionStatusUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionResponse execute(String id, UpdateTransactionRequest request) {
        validateRequest(id, request);

        TransactionId transactionId = TransactionId.from(id);
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));

        if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            TransactionStatus newStatus = TransactionStatus.fromString(request.getStatus());
            transaction.updateStatus(newStatus);
        }

        if (request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
            Description newDescription = new Description(request.getDescription());
            transaction.updateDescription(newDescription);
        }

        Transaction updatedTransaction = transactionRepository.save(transaction);

        return mapToResponse(updatedTransaction);
    }

    private void validateRequest(String id, UpdateTransactionRequest request) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction ID cannot be null or empty");
        }
        if (request == null) {
            throw new IllegalArgumentException("Update transaction request cannot be null");
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
