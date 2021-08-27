package com.bok.bank.integration.dto;

import com.bok.bank.integration.util.Money;

import java.math.BigDecimal;
import java.time.LocalDate;

public class WiredTransferRequestDTO {
    public String destinationIBAN;
    public String beneficiary;
    public BigDecimal amount;
    public LocalDate executionDate;
    public boolean instantTransfer;
}
