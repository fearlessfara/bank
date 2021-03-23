package com.bok.bank.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Currency;

@Embeddable
public class Money implements Comparable<Money>, Serializable {

    public final static int DEFAULT_SCALE = Scale.CENTS.getValue();
    public final static Money ZERO = new Money(new BigDecimal(BigInteger.ZERO, DEFAULT_SCALE));
    public final static RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
    public final static Currency DEFAULT_CURRENCY = Currency.getInstance("EUR");

    private BigDecimal value;

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

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Money money = (Money) o;

        return new EqualsBuilder().append(value, money.value).append(currency, money.currency).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(value).append(currency).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("value", value)
                .append("currency", currency)
                .toString();
    }

    @Override
    public int compareTo(Money other) {
        return value.compareTo(other.getValue());
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
