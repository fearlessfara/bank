package com.bok.bank.repository;

import com.bok.bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    @Query("SELECT a.username, a.email, a.icc, a.mobile, a.status, a.type, a.name, a.middleName, a.surname FROM Account a WHERE a.id = :accountId")
    Optional<Projection.AccountInfo> findAccountInfoByAccountId(Long accountId);

    public static class Projection {

        public interface AccountId{
            Long getId();
        }

        public interface AccountInfo{
            String getUsername();
            String getEmail();
            String getIcc();
            String getMobile();
            Account.Status getStatus();
            Account.Type getType();
            String getName();
            String getMiddleName();
            String getSurname();

        }
    }

}
