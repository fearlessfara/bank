package com.bok.bank.repository;

import com.bok.bank.model.WiredTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WiredTransferRepository extends JpaRepository<WiredTransfer, Long> {

    List<WiredTransfer> findByInstantTransferIsFalseAndExecutionDateIsAndProcessedIsFalse(LocalDate executionDate);
}
