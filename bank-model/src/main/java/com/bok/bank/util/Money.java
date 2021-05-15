package com.bok.bank.util;

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

    public final static int DEFAULT_SCALE = Scale.MICRO.getValue();
    public final static Money ZERO = new Money(new BigDecimal(BigInteger.ZERO, DEFAULT_SCALE));
    public final static RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
    public final static Currency DEFAULT_CURRENCY = Currency.getInstance("EUR");

    private BigDecimal value;

    private Currency currency;

    protected Money() {

    }

    private Money(long value, Scale scale) {
        this.currency = DEFAULT_CURRENCY;
        this.value = BigDecimal.valueOf(value, scale.getValue()).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING);
    }

    private Money(long value, Scale scale, Currency currency) {
        this.currency = currency;
        this.value = BigDecimal.valueOf(value, scale.getValue()).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING);
    }


    public Money(Money copy) {
        this.currency = copy.currency;
        this.value = new BigDecimal(copy.value.unscaledValue(), copy.value.scale());
    }

    public Money(BigDecimal value) {
        this.currency = DEFAULT_CURRENCY;
        this.value = value;
    }

    public Money(BigDecimal value, Currency currency) {
        this.currency = currency;
        this.value = value;
    }


    public BigDecimal getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Money plus(Money other) {
        if (other == null) {
            return this;
        }
        checkCurrency(other.currency);
        BigDecimal result = value.add(other.getValue());
        return new Money(result, other.currency);
    }

    public Money subtract(Money other) {
        if (other == null) {
            return this;
        }
        checkCurrency(other.currency);
        BigDecimal result = value.subtract(other.getValue());
        return new Money(result, other.currency);

    }

    public Money divide(long other, Scale scale, RoundingMode roundingMode) {
        BigDecimal result = value.divide(BigDecimal.valueOf(other), roundingMode);
        return new Money(result.setScale(scale.getValue(), roundingMode), this.currency);
    }

    public Money divide(long other) {
        return divide(other, Scale.MICRO, DEFAULT_ROUNDING);
    }

    public Money multiply(long other, Scale scale, RoundingMode roundingMode) {
        BigDecimal result = value.multiply(BigDecimal.valueOf(other));
        return new Money(result.setScale(scale.getValue(), roundingMode), this.currency);
    }

    public Money multiply(long other) {
        return multiply(other, Scale.MICRO, DEFAULT_ROUNDING);
    }

    public Money divide(BigDecimal other, Scale scale, RoundingMode roundingMode) {
        BigDecimal result = value.divide(other, roundingMode);
        return new Money(result.setScale(scale.getValue(), roundingMode), this.currency);
    }

    public Money divide(BigDecimal other) {
        return divide(other, Scale.MICRO, DEFAULT_ROUNDING);
    }

    public Money multiply(BigDecimal other, Scale scale, RoundingMode roundingMode) {
        BigDecimal result = value.multiply(other);
        return new Money(result.setScale(scale.getValue(), roundingMode), this.currency);
    }

    public Money multiply(BigDecimal other) {
        return multiply(other, Scale.MICRO, DEFAULT_ROUNDING);
    }

    public Money abs() {
        return new Money(value.abs(), currency);
    }

    public Long toCents() {
        return this.value.setScale(Scale.CENTS.getValue(), DEFAULT_ROUNDING).
                movePointRight(Scale.CENTS.getValue()).
                longValue();
    }

    public Money roundingCents() {
        return roundingCents(DEFAULT_ROUNDING);
    }

    public Money roundingCents(RoundingMode roundingMode) {
        return new Money(this.value.setScale(Scale.CENTS.getValue(), roundingMode).setScale(Scale.MICRO.getValue(), roundingMode), currency);
    }

    public Double toDoubleCents() {
        return this.value.setScale(Money.Scale.CENTS.getValue(), Money.DEFAULT_ROUNDING).doubleValue();
    }

    public boolean isGreaterThan(Money other) {
        return compareTo(other) > 0;
    }

    public boolean isGreaterOrEqualsThan(Money other) {
        return compareTo(other) >= 0;
    }

    public boolean isLessThan(Money other) {
        return compareTo(other) < 0;
    }

    public boolean isLessOrEqualThan(Money other) {
        return compareTo(other) <= 0;
    }

    public static Money money(Money copy) {
        return new Money(copy);
    }

    public static Money money(long value, Scale scale, Currency currency) {
        return new Money(value, scale, currency);
    }

    public static Money money(long value, Scale scale) {
        return new Money(value, scale);
    }


    public static Money money(BigDecimal bigDecimal, Currency currency) {
        return new Money(bigDecimal, currency);
    }

    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public static Money max(final Money m1, final Money m2) {
        if (m1.isLessThan(m2)) {
            return m2;
        }
        return m1;
    }

    public static Money min(final Money m1, final Money m2) {
        if (m1.isGreaterThan(m2)) {
            return m2;
        }
        return m1;
    }

    private void checkCurrency(Currency currency) {
        if (!this.currency.equals(currency))
            throw new IllegalStateException("Currency mismatch");

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

        public void setValue(int value) {
            this.value = value;
        }
    }
}
