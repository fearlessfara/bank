package com.bok.bank.repository;

import com.bok.bank.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findByAccount_Id(Long accountId);

    Optional<BankAccount> findByAccount_IdAndStatus(Long accountId, BankAccount.Status status);
}
