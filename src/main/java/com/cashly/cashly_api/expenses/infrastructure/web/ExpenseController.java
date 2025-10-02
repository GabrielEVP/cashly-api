package com.cashly.cashly_api.expenses.infrastructure.web;

import com.cashly.cashly_api.expenses.application.dto.CreateExpenseRequest;
import com.cashly.cashly_api.expenses.application.dto.ExpenseResponse;
import com.cashly.cashly_api.expenses.application.dto.UpdateExpenseRequest;
import com.cashly.cashly_api.expenses.application.usecases.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final CreateExpenseUseCase createExpenseUseCase;
    private final UpdateExpenseUseCase updateExpenseUseCase;
    private final DeleteExpenseUseCase deleteExpenseUseCase;
    private final GetExpenseByIdUseCase getExpenseByIdUseCase;
    private final GetExpensesByUserUseCase getExpensesByUserUseCase;

    public ExpenseController(CreateExpenseUseCase createExpenseUseCase,
                           UpdateExpenseUseCase updateExpenseUseCase,
                           DeleteExpenseUseCase deleteExpenseUseCase,
                           GetExpenseByIdUseCase getExpenseByIdUseCase,
                           GetExpensesByUserUseCase getExpensesByUserUseCase) {
        this.createExpenseUseCase = createExpenseUseCase;
        this.updateExpenseUseCase = updateExpenseUseCase;
        this.deleteExpenseUseCase = deleteExpenseUseCase;
        this.getExpenseByIdUseCase = getExpenseByIdUseCase;
        this.getExpensesByUserUseCase = getExpensesByUserUseCase;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@RequestBody CreateExpenseRequest request) {
        ExpenseResponse response = createExpenseUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable String id,
                                                        @RequestBody UpdateExpenseRequest request) {
        ExpenseResponse response = updateExpenseUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable String id) {
        deleteExpenseUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable String id) {
        ExpenseResponse response = getExpenseByIdUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getExpensesByUser(@RequestParam String userId) {
        List<ExpenseResponse> responses = getExpensesByUserUseCase.execute(userId);
        return ResponseEntity.ok(responses);
    }
}
