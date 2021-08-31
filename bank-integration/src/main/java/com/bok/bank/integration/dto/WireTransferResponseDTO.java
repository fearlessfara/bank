package com.bok.bank.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class WireTransferResponseDTO {
    public Boolean accepted;
    public String reason;
}
