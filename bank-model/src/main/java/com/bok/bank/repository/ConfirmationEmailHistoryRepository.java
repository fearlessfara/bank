package com.bok.bank.repository;

import com.bok.bank.model.ConfirmationEmailHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationEmailHistoryRepository extends JpaRepository<ConfirmationEmailHistory, Long> {

    boolean existsByConfirmationToken(String confirmationToken);
}
