package com.bok.bank.repository;

import com.bok.bank.model.BankAccount;
import com.bok.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    int deleteByFromBankAccount(BankAccount toBankAccount);
}
