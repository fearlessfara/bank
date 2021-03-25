package com.bok.bank.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Currency;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckPaymentAmountRequestDTO {
    public Currency currency;
    public BigDecimal amount;

    public CheckPaymentAmountRequestDTO() {
        /* Jackson */
    }

    public CheckPaymentAmountRequestDTO(Currency currency, BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("currency", currency)
                .append("amount", amount)
                .toString();
    }
}
