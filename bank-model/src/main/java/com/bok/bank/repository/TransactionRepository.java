package com.bok.bank.repository;

import com.bok.bank.model.BankAccount;
import com.bok.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    int deleteByFromBankAccount(BankAccount toBankAccount);

    Optional<Transaction> findByPublicId(UUID publicId);
}
