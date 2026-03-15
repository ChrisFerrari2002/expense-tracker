package com.chrisferrari.expense_tracker.service;

import com.chrisferrari.expense_tracker.dto.MonthlySummaryResponse;
import com.chrisferrari.expense_tracker.entity.Category;
import com.chrisferrari.expense_tracker.entity.Expense;
import com.chrisferrari.expense_tracker.exception.ResourceNotFoundException;
import com.chrisferrari.expense_tracker.repository.ExpenseRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Spesa non trovata con id:" + id));
    }

    public List<Expense> getExpensesByCategory(Category category) {
        return expenseRepository.findByCategory(category);
    }

    public List<Expense> getExpensesByDateRange(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return expenseRepository.findByDateBetween(startDate, endDate);
    }

    public Expense createExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public Expense updateExpense(Long id, Expense updatedExpense) {
        Expense expense = expenseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Spesa non trovata con id: " + id));
            
            expense.setTitle(updatedExpense.getTitle());
            expense.setAmount(updatedExpense.getAmount());
            expense.setCategory(updatedExpense.getCategory());
            expense.setDate(updatedExpense.getDate());
            expense.setNote(updatedExpense.getNote());
            
            return expenseRepository.save(expense);
                
    }

    public void deleteExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Spesa non trovata con id: " + id));

        expenseRepository.delete(expense);    
    }

    public MonthlySummaryResponse getMonthlySummaryResponse(int year, int month) {
        List<Expense> expenses = getExpensesByDateRange(year, month);

        BigDecimal total = expenses.stream()
            .map(Expense::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new MonthlySummaryResponse(year, month, total);
    }
}
