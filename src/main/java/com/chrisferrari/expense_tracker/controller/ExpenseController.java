package com.chrisferrari.expense_tracker.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.chrisferrari.expense_tracker.dto.MonthlySummaryResponse;
import com.chrisferrari.expense_tracker.entity.Category;
import com.chrisferrari.expense_tracker.entity.Expense;
import com.chrisferrari.expense_tracker.service.ExpenseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/expenses")
@Tag(name = "Expenses", description = "Expenses management")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @Operation(summary = "Get all expenses, or filter them by category or date range")
    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses(
        @RequestParam(required = false) Category category,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer month) {
        
        if (year != null && month != null ) {
            return ResponseEntity.ok(expenseService.getExpensesByDateRange(year, month));
        }

        if (category != null) {
            return ResponseEntity.ok(expenseService.getExpensesByCategory(category));
        }

        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    @Operation(summary = "Get expense by id")
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.getExpenseById(id));
    } 

    @Operation(summary = "Get monthly summary")
    @GetMapping("/summary")
    public ResponseEntity<MonthlySummaryResponse> getMonthlySummary(
        @RequestParam int year,
        @RequestParam int month
    ) {
        return ResponseEntity.ok(expenseService.getMonthlySummaryResponse(year, month));
    }

    @Operation(summary = "Create a new expense")
    @PostMapping
    public ResponseEntity<Expense> createExpense(@Valid @RequestBody Expense expense) {
        Expense createdExpense = expenseService.createExpense(expense);
        return ResponseEntity.ok(createdExpense);
    }

    @Operation(summary = "Update an existing expense")
    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @Valid @RequestBody Expense expense) {
        Expense updatedExpense = expenseService.updateExpense(id, expense);
        return ResponseEntity.ok(updatedExpense);
    }

    @Operation(summary = "Delete an expense")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }


}
