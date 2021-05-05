package com.bok.bank.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountInfoDTO {
    public String email;
    public String icc;
    public String mobile;
    public String status;
    public String type;
    public String fullName;

    public AccountInfoDTO() {
    }

    public AccountInfoDTO(String email, String icc, String mobile, String status, String type, String fullName) {
        this.email = email;
        this.icc = icc;
        this.mobile = mobile;
        this.status = status;
        this.type = type;
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("email", email)
                .append("icc", icc)
                .append("mobile", mobile)
                .append("status", status)
                .append("type", type)
                .append("fullName", fullName)
                .toString();
    }
}
