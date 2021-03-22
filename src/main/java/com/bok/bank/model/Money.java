package com.bok.bank.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Currency;

@Embeddable
public class Money {

    public final static int DEFAULT_SCALE = Scale.CENTS.getValue();
    public final static Money ZERO = new Money(new BigDecimal(BigInteger.ZERO, DEFAULT_SCALE));
    public final static RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
    public final static Currency DEFAULT_CURRENCY = Currency.getInstance("EUR");

    @Column(precision = 19, scale = 6, updatable = true)
    private BigDecimal value;

    @Column(updatable = true, length = 3)
    private Currency currency;

    protected Money() {

    }

    public Money(BigDecimal bigDecimal) {
        this.value = bigDecimal;
        this.currency = DEFAULT_CURRENCY;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public int getScale() {
        return value.scale();
    }

    public enum Scale {

        UNIT(0),
        CENTS(2),
        MICRO(6);

        private int value;

        private Scale(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }
}
