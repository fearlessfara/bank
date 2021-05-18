package com.bok.bank.integration.dto;

import com.bok.bank.integration.util.Money;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizationRequestDTO {
    public Long accountId;
    public Money money;
    public String fromMarket;
}
