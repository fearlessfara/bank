package com.bok.bank.integration.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Money implements Serializable {
    public Currency currency;
    public BigDecimal amount;

    public com.bok.bank.integration.grpc.Money toGrpcMoney() {
        com.bok.bank.integration.grpc.Money.Builder moneyBuilder = com.bok.bank.integration.grpc.Money.newBuilder();
        moneyBuilder.setAmount(amount.doubleValue());

        com.bok.bank.integration.grpc.Currency currencyBuilder = com.bok.bank.integration.grpc.Currency.valueOf(currency.getCurrencyCode());
        moneyBuilder.setCurrency(currencyBuilder);
        return moneyBuilder.build();
    }

    @Override
    public String toString() {
        return "" + amount + " " + currency.getCurrencyCode();
    }
}
