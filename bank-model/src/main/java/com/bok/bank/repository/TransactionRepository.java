package com.bok.bank.repository;

import com.bok.bank.model.Account;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    int deleteByFromBankAccount(BankAccount toBankAccount);

    List<Transaction> findDistinctByTransactionOwnerOrFromBankAccountOrToBankAccountOrderByTimestampDesc(Account account, BankAccount bankAccount, BankAccount bankAccount2);

    Optional<Transaction> findByPublicId(String publicId);

    List<Transaction> findTransactionsByCard_Id(Long cardId);

    List<Transaction> findTransactionsByStatusAndTypeAndExecutionDate(Transaction.Status status, Transaction.Type type, LocalDate localDate);
}
