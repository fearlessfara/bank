package com.bok.bank.repository;

import com.bok.bank.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Currency;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findByAccount_Id(Long accountId);
    boolean existsByAccount_Id(Long accountId);

    Optional<BankAccount> findByAccount_IdAndStatus(Long accountId, BankAccount.Status status);

    @Query("SELECT ba.id as id, ba.name as bankAccountName, ba.currency as currency, ba.status as status FROM BankAccount ba WHERE ba.account.id = :accountId")
    Optional<Projections.BankAccountBasicInfo> findBankAccountBasicInfoByAccountId(Long accountId);

    public class Projections {
        public interface BankAccountBasicInfo {
            Long getId();

            String getBankAccountName();

            Currency getCurrency();

            BankAccount.Status getStatus();
        }
    }
}
