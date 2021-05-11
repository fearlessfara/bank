package com.bok.bank.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Currency;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BankAccountDTO {
    public String name;
    public String label;
    public Currency currency;

    public BankAccountDTO() {
    }

    public BankAccountDTO(String name, String label, Currency currency) {
        this.name = name;
        this.label = label;
        this.currency = currency;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("label", label)
                .append("currency", currency)
                .toString();
    }
}
