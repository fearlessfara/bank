package com.bok.bank.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Currency;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardInfoDTO {
    public String name;
    public String cardStatus;
    public String type;
    public Currency currency;
    public String label;
    public String maskedPan;
    public Integer cvv;

    public CardInfoDTO() {
    }

    public CardInfoDTO(String name, String cardStatus, String type, Currency currency, String label, String maskedPan, Integer cvv) {
        this.name = name;
        this.cardStatus = cardStatus;
        this.type = type;
        this.currency = currency;
        this.label = label;
        this.maskedPan = maskedPan;
        this.cvv = cvv;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("cardStatus", cardStatus)
                .append("type", type)
                .append("currency", currency)
                .append("label", label)
                .append("maskedPan", maskedPan)
                .append("cvv", cvv)
                .toString();
    }
}
