package com.bok.bank.helper;

import com.bok.bank.model.ExchangeCurrencyValue;
import com.bok.bank.repository.ExchangeCurrencyValueRepository;
import com.bok.bank.util.Money;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.bok.bank.util.Constants.CURRENCIES_SAVED;

@Component
@Slf4j
public class ExchangeCurrencyAmountHelper {

    @Autowired
    ExchangeCurrencyValueRepository exchangeCurrencyValueRepository;

    public Money convertAmount(Money fromAmount, Money toAmount) {
        if (!CURRENCIES_SAVED.contains(fromAmount.getCurrency().getCurrencyCode())) {
            ExchangeCurrencyValue exchangeCurrencyValue = exchangeCurrencyValueRepository.findByBaseCurrency(Money.DEFAULT_CURRENCY.getCurrencyCode());
            BigDecimal conversionRateToEUR = exchangeCurrencyValue.getConversion_rates().get(fromAmount.getCurrency().getCurrencyCode());
            BigDecimal conversionRateToRequiredCurrency = exchangeCurrencyValue.getConversion_rates().get(toAmount.getCurrency().getCurrencyCode());
            return fromAmount.divide(conversionRateToEUR).multiply(conversionRateToRequiredCurrency);
        }
        ExchangeCurrencyValue exchangeCurrencyValue = exchangeCurrencyValueRepository.findByBaseCurrency(fromAmount.getCurrency().getCurrencyCode());
        BigDecimal conversionRate = exchangeCurrencyValue.getConversion_rates().get(toAmount.getCurrency().getCurrencyCode());
        return fromAmount.multiply(conversionRate);
    }
}
