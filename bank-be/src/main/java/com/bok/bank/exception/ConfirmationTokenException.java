package com.bok.bank.exception;

public class ConfirmationTokenException extends RuntimeException {

    public ConfirmationTokenException() {
        super(ErrorCode.CONFIRMATION_TOKEN_NOT_VALID.name());
    }

    public ConfirmationTokenException(String message) {
        super(message);
    }

}
