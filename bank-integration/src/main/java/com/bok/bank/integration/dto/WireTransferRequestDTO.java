package com.bok.bank.integration.dto;

import com.bok.bank.integration.util.Money;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class WireTransferRequestDTO {
    public String destinationIBAN;
    public String beneficiary;
    public String causal;
    public Money money;
    public LocalDate executionDate;
    public boolean instantTransfer;
}
