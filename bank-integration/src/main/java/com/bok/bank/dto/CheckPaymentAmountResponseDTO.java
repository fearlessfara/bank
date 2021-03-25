package com.bok.bank.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckPaymentAmountResponseDTO {
    public Boolean available;
    public String reason;

    public CheckPaymentAmountResponseDTO() {
    }

    public CheckPaymentAmountResponseDTO(Boolean available, String reason) {
        this.available = available;
        this.reason = reason;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("available", available)
                .append("reason", reason)
                .toString();
    }
}
