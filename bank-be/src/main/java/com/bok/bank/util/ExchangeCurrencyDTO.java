package com.bok.bank.util;

import com.bok.bank.model.ExchangeCurrencyValueHistory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "time_last_update_unix",
        "time_next_update_unix",
        "base_code",
        "conversion_rates"
})
@Slf4j
public class ExchangeCurrencyDTO {

    @JsonProperty("time_last_update_unix")
    public Long time_last_update_unix;

    @JsonProperty("time_next_update_unix")
    public Long time_next_update_unix;

    @JsonProperty("base_code")
    public Currency base_code;

    @JsonProperty("conversion_rates")
    public Map<String, BigDecimal> conversion_rates = new HashMap<>();

    public ExchangeCurrencyDTO() {
    }

    public ExchangeCurrencyValueHistory toExchangeCurrencyValues(String baseCurrency){
        this.conversion_rates.forEach((s, bigDecimal) -> this.conversion_rates.replace(s, (bigDecimal.scale()==0) ? bigDecimal.setScale(4, BigDecimal.ROUND_FLOOR) : bigDecimal));
        return new ExchangeCurrencyValueHistory(Instant.ofEpochSecond(this.time_last_update_unix), Instant.ofEpochSecond(this.time_next_update_unix), baseCurrency, this.conversion_rates);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("time_last_update_unix", time_last_update_unix)
                .append("conversion_rates", conversion_rates)
                .toString();
    }
}
