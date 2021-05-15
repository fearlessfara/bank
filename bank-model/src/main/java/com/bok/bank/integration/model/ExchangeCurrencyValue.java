package com.bok.bank.integration.model;

import com.bok.bank.integration.util.JsonType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Entity
public class ExchangeCurrencyValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Instant time_last_update_unix;

    @Column
    private Instant time_next_update_unix;

    @Column(unique = true, nullable = false)
    private String baseCurrency;

    @Column(length = 1024 * 1024)
    @Type(type = "com.bok.bank.integration.util.JsonType",
            parameters = {@org.hibernate.annotations.Parameter(name = JsonType.CLASSNAME, value = "java.util.Map")}
    )
    private Map<String, BigDecimal> conversion_rates = new HashMap<>();

    public ExchangeCurrencyValue(Instant time_last_update_unix, Instant time_next_update_unix, String baseCurrency, Map<String, BigDecimal> conversion_rates) {
        this.time_last_update_unix = time_last_update_unix;
        this.time_next_update_unix = time_next_update_unix;
        this.baseCurrency = baseCurrency;
        this.conversion_rates = conversion_rates;
    }

    public ExchangeCurrencyValue() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTime_last_update_unix() {
        return time_last_update_unix;
    }

    public void setTime_last_update_unix(Instant time_last_update_unix) {
        this.time_last_update_unix = time_last_update_unix;
    }

    public Instant getTime_next_update_unix() {
        return time_next_update_unix;
    }

    public void setTime_next_update_unix(Instant time_next_update_unix) {
        this.time_next_update_unix = time_next_update_unix;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Map<String, BigDecimal> getConversion_rates() {
        return this.conversion_rates;
    }

    public void setConversion_rates(Map<String, BigDecimal> conversion_rates) {
        this.conversion_rates = conversion_rates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ExchangeCurrencyValue that = (ExchangeCurrencyValue) o;

        return new EqualsBuilder().append(id, that.id).append(time_last_update_unix, that.time_last_update_unix).append(time_next_update_unix, that.time_next_update_unix).append(baseCurrency, that.baseCurrency).append(conversion_rates, that.conversion_rates).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(time_last_update_unix).append(time_next_update_unix).append(baseCurrency).append(conversion_rates).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("time_last_update_unix", time_last_update_unix)
                .append("time_next_update_unix", time_next_update_unix)
                .append("baseCurrency", baseCurrency)
                .append("conversion_rates", conversion_rates)
                .toString();
    }
}
