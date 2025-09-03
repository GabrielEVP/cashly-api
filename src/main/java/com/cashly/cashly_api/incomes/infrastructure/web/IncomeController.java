package com.cashly.cashly_api.incomes.infrastructure.web;

import com.cashly.cashly_api.incomes.application.dto.CreateIncomeRequest;
import com.cashly.cashly_api.incomes.application.dto.IncomeResponse;
import com.cashly.cashly_api.incomes.application.dto.UpdateIncomeRequest;
import com.cashly.cashly_api.incomes.application.usecases.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incomes")
public class IncomeController {

    private final CreateIncomeUseCase createIncomeUseCase;
    private final UpdateIncomeUseCase updateIncomeUseCase;
    private final DeleteIncomeUseCase deleteIncomeUseCase;
    private final GetIncomeByIdUseCase getIncomeByIdUseCase;
    private final GetIncomesByUserUseCase getIncomesByUserUseCase;

    public IncomeController(CreateIncomeUseCase createIncomeUseCase,
                           UpdateIncomeUseCase updateIncomeUseCase,
                           DeleteIncomeUseCase deleteIncomeUseCase,
                           GetIncomeByIdUseCase getIncomeByIdUseCase,
                           GetIncomesByUserUseCase getIncomesByUserUseCase) {
        this.createIncomeUseCase = createIncomeUseCase;
        this.updateIncomeUseCase = updateIncomeUseCase;
        this.deleteIncomeUseCase = deleteIncomeUseCase;
        this.getIncomeByIdUseCase = getIncomeByIdUseCase;
        this.getIncomesByUserUseCase = getIncomesByUserUseCase;
    }

    @PostMapping
    public ResponseEntity<IncomeResponse> createIncome(@RequestBody CreateIncomeRequest request) {
        try {
            IncomeResponse response = createIncomeUseCase.execute(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeResponse> updateIncome(@PathVariable String id, 
                                                      @RequestBody UpdateIncomeRequest request) {
        try {
            IncomeResponse response = updateIncomeUseCase.execute(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable String id) {
        try {
            deleteIncomeUseCase.execute(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeResponse> getIncomeById(@PathVariable String id) {
        try {
            IncomeResponse response = getIncomeByIdUseCase.execute(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<IncomeResponse>> getIncomesByUser(@RequestParam String userId) {
        try {
            List<IncomeResponse> responses = getIncomesByUserUseCase.execute(userId);
            return ResponseEntity.ok(responses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}