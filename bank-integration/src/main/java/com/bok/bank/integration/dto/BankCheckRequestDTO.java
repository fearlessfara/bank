package com.bok.bank.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankCheckRequestDTO {
    public String fiscalCode;
    public String vatNumber;
    public Boolean business;
}
