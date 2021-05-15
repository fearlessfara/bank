package com.bok.bank.integration.dto;

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
public class CardDTO {
    public String name;
    public String type;
    public String label;

}
