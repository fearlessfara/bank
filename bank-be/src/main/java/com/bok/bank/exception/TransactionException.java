package com.bok.bank.exception;

public class TransactionException extends RuntimeException {
    public TransactionException() {
        super(ErrorCode.AMOUNT_NOT_AVAILABLE.name());
    }

    public TransactionException(String message) {
        super(message);
    }
}
