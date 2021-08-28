package com.bok.bank.repository;

import com.bok.bank.model.BankAccount;
import com.bok.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    int deleteByFromBankAccount(BankAccount toBankAccount);

    @Query("select t from Transaction t where t.transactionOwner.id = :accountId or t.toBankAccount.accountId = :accountId order by t.timestamp desc")
    List<Transaction> findByAccountId(Long accountId);

    Optional<Transaction> findByPublicId(String publicId);

    List<Transaction> findTransactionsByCard_Id(Long cardId);

    List<Transaction> findTransactionsByStatusAndTypeAndExecutionDate(Transaction.Status status, Transaction.Type type, LocalDate localDate);
}
