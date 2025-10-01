package com.cashly.cashly_api.transactions.application.usecases;

import com.cashly.cashly_api.transactions.application.dto.CreateTransactionRequest;
import com.cashly.cashly_api.transactions.application.dto.TransactionResponse;
import com.cashly.cashly_api.transactions.application.ports.TransactionRepository;
import com.cashly.cashly_api.transactions.domain.entities.Transaction;
import com.cashly.cashly_api.transactions.domain.services.TransactionService;
import com.cashly.cashly_api.transactions.domain.valueobjects.*;
import org.springframework.stereotype.Service;

@Service
public class CreateTransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;

    public CreateTransactionUseCase(TransactionRepository transactionRepository,
                                   TransactionService transactionService) {
        this.transactionRepository = transactionRepository;
        this.transactionService = transactionService;
    }

    public TransactionResponse execute(CreateTransactionRequest request) {
        validateRequest(request);

        TransactionId id = TransactionId.generate();
        TransactionType type = TransactionType.fromString(request.getType());
        TransactionStatus status = TransactionStatus.PENDING;
        Amount amount = new Amount(request.getAmount());
        Description description = new Description(request.getDescription());
        TransactionDate transactionDate = request.getTransactionDate() != null
            ? new TransactionDate(request.getTransactionDate())
            : TransactionDate.now();

        Transaction transaction = new Transaction(
            id,
            request.getUserId(),
            type,
            status,
            amount,
            request.getCurrency(),
            description,
            transactionDate,
            request.getSourceAccountId(),
            request.getDestinationAccountId(),
            request.getExpenseId(),
            request.getIncomeId()
        );

        transactionService.validateTransactionIntegrity(transaction);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return mapToResponse(savedTransaction);
    }

    private void validateRequest(CreateTransactionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Create transaction request cannot be null");
        }
        if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (request.getType() == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }
        if (request.getAmount() == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (request.getCurrency() == null || request.getCurrency().trim().isEmpty()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
        if (request.getDescription() == null) {
            throw new IllegalArgumentException("Description cannot be null");
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
