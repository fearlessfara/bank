package com.bok.bank.integration.dto;

import com.bok.bank.integration.util.Money;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizationRequestDTO {
    public Long accountId;
    public UUID extTransactionId;
    public Money money;
    public String fromMarket;
}
