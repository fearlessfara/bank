package com.bok.bank.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizationResponseDTO {
    public Boolean authorized = Boolean.FALSE;
    public String reason;
    public Long authorizationId;
}
