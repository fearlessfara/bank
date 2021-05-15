package com.bok.bank.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardInfoDTO {
    public Long cardId;
    public String name;
    public String cardStatus;
    public String type;
    public String label;
    public String maskedPan;
    public Integer cvv;

    public CardInfoDTO(Long cardId, String name, String cardStatus, String type, String label, String maskedPan) {
        this.cardId = cardId;
        this.name = name;
        this.cardStatus = cardStatus;
        this.type = type;
        this.label = label;
        this.maskedPan = maskedPan;
    }
}
