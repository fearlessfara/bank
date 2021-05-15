package com.bok.bank.integration.repository;

import com.bok.bank.integration.model.ConfirmationEmailHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationEmailHistoryRepository extends JpaRepository<ConfirmationEmailHistory, Long> {

    boolean existsByConfirmationToken(String confirmationToken);

    Optional<ConfirmationEmailHistory> findByConfirmationTokenAndResourceType(String confirmationToken, ConfirmationEmailHistory.ResourceType resourceType);

}
