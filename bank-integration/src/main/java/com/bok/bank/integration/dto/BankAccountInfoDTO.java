package com.bok.bank.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.util.Currency;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BankAccountInfoDTO {
    public String email;
    public String bankAccountName;
    public String IBAN;
    public String label;
    public Currency currency;
    public BigDecimal blockedAmount;
    public BigDecimal availableAmount;
    public String status;

    public BankAccountInfoDTO(String email, String bankAccountName, String IBAN, String label, Currency currency, BigDecimal blockedAmount, BigDecimal availableAmount, String status) {
        this.email = email;
        this.bankAccountName = bankAccountName;
        this.IBAN = IBAN;
        this.label = label;
        this.currency = currency;
        this.blockedAmount = blockedAmount;
        this.availableAmount = availableAmount;
        this.status = status;
    }

    public BankAccountInfoDTO() {
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("email", email)
                .append("bankAccountName", bankAccountName)
                .append("IBAN", IBAN)
                .append("label", label)
                .append("currency", currency)
                .append("blockedAmount", blockedAmount)
                .append("availableAmount", availableAmount)
                .append("status", status)
                .toString();
    }
}