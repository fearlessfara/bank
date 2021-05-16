package com.bok.bank.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Currency;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

}
