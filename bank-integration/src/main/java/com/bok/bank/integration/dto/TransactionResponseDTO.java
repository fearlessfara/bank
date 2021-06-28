package com.bok.bank.integration.dto;

import com.bok.bank.integration.util.Money;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Instant;

public class TransactionResponseDTO {
    public String publicId;
    public String type;
    public String status;
    public Instant timestamp;
    public Money amount;

    public TransactionResponseDTO(String publicId, String type, String status, Instant timestamp) {
        this.publicId = publicId;
        this.type = type;
        this.status = status;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("publicId", publicId)
                .append("type", type)
                .append("status", status)
                .append("timestamp", timestamp)
                .append("amount", amount)
                .toString();
    }
}
