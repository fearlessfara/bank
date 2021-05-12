package com.bok.bank.util.exception;

public class AccountException extends RuntimeException{
    public AccountException() {
        super(ErrorCode.ACCOUNT_NOT_FOUND.name());
    }

    public AccountException(String message) {
        super(message);
    }

}
