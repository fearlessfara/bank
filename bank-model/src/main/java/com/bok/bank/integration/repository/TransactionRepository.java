package com.bok.bank.integration.repository;

import com.bok.bank.integration.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
