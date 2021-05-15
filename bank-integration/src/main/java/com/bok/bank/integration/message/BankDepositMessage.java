package com.bok.bank.integration.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankDepositMessage implements Serializable {
    public BigDecimal amount;
    public String currency;
    public Long accountId;
    public String fromMarket;

}
