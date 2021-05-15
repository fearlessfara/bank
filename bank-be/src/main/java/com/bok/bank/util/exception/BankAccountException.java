package com.bok.bank.util.exception;

public class BankAccountException extends RuntimeException {

    public BankAccountException() {
        super(ErrorCode.BANK_ACCOUNT_NOT_FOUND.name());
    }

    public BankAccountException(String message) {
        super(message);
    }
}
