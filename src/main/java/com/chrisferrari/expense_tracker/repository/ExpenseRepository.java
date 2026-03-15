package com.chrisferrari.expense_tracker.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chrisferrari.expense_tracker.entity.Category;
import com.chrisferrari.expense_tracker.entity.Expense;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByCategory(Category category);

    List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);

}
