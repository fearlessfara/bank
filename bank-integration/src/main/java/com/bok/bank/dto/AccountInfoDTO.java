package com.bok.bank.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class AccountInfoDTO {
    public String username;
    public String email;
    public String icc;
    public String mobile;
    public String status;
    public String type;
    public String fullName;

    public AccountInfoDTO() {
    }

    public AccountInfoDTO(String username, String email, String icc, String mobile, String status, String type, String fullName) {
        this.username = username;
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
                .append("username", username)
                .append("email", email)
                .append("icc", icc)
                .append("mobile", mobile)
                .append("status", status)
                .append("type", type)
                .append("fullName", fullName)
                .toString();
    }
}
