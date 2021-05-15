package com.bok.bank.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Currency;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardInfoDTO {
    public Long cardId;
    public String name;
    public String cardStatus;
    public String type;
    public String label;
    public String maskedPan;
    public Integer cvv;

    public CardInfoDTO() {
    }

    public CardInfoDTO(Long cardId, String name, String cardStatus, String type, String label, String maskedPan) {
        this.cardId = cardId;
        this.name = name;
        this.cardStatus = cardStatus;
        this.type = type;
        this.label = label;
        this.maskedPan = maskedPan;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("cardStatus", cardStatus)
                .append("type", type)
                .append("label", label)
                .append("maskedPan", maskedPan)
                .append("cvv", cvv)
                .toString();
    }
}
