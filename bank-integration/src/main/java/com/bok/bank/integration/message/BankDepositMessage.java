package com.bok.bank.integration.message;

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
public class BankDepositMessage implements Serializable {
    public Money money;
    public Long accountId;
    public String fromMarket;
    public UUID extTransactionId;

}
