package com.bok.bank.integration.dto;

import com.bok.bank.integration.util.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
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
    public UUID transactionId;
    public String type;

    public TransactionDTO(Money transactionAmount, Long accountId, String fromMarket, String type) {
        this.transactionAmount = transactionAmount;
        this.accountId = accountId;
        this.fromMarket = fromMarket;
        this.type = type;
    }
}
