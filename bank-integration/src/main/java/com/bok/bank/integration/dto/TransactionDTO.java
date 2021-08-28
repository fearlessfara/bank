package com.bok.bank.integration.dto;

import com.bok.bank.integration.util.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO implements Serializable {
    public Money transactionAmount;
    public Long accountId;
    public String fromMarket;
    public String destinationIBAN;
    public String beneficiary;
    public UUID extTransactionId;
    public LocalDate executionDate;
    public boolean instantTransfer;
    public String type;

    public TransactionDTO(Money transactionAmount, Long accountId, String fromMarket, String type) {
        this.transactionAmount = transactionAmount;
        this.accountId = accountId;
        this.fromMarket = fromMarket;
        this.type = type;
    }

    public TransactionDTO(Money transactionAmount, Long accountId, String fromMarket, UUID extTransactionId, String type) {
        this.transactionAmount = transactionAmount;
        this.accountId = accountId;
        this.fromMarket = fromMarket;
        this.extTransactionId = extTransactionId;
        this.type = type;
    }

    public TransactionDTO(Money transactionAmount, Long accountId, String destinationIBAN, String beneficiary, boolean instantTransfer, LocalDate executionDate, String type) {
        this.transactionAmount = transactionAmount;
        this.accountId = accountId;
        this.destinationIBAN = destinationIBAN;
        this.beneficiary = beneficiary;
        this.executionDate = executionDate;
        this.instantTransfer = instantTransfer;
        this.type = type;
    }
}
