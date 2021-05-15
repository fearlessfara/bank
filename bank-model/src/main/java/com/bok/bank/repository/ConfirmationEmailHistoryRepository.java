package com.bok.bank.repository;

import com.bok.bank.model.Account;
import com.bok.bank.model.ConfirmationEmailHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationEmailHistoryRepository extends JpaRepository<ConfirmationEmailHistory, Long> {

    boolean existsByConfirmationToken(String confirmationToken);

    Optional<ConfirmationEmailHistory> findByConfirmationTokenAndResourceType(String confirmationToken, ConfirmationEmailHistory.ResourceType resourceType);

    int deleteByAccount(Account account);
}
