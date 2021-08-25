package com.bok.bank.exception;

public class CardException extends RuntimeException {
    public CardException() {
        super(ErrorCode.CARD_NOT_FOUND.name());
    }

    public CardException(String message) {
        super(message);
    }

}
