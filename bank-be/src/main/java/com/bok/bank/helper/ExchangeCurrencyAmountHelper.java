package com.bok.bank.helper;

import com.bok.bank.model.ExchangeCurrencyValue;
import com.bok.bank.repository.ExchangeCurrencyValueRepository;
import com.bok.bank.util.Constants;
import com.bok.bank.util.Money;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Currency;

@Component
@Slf4j
public class ExchangeCurrencyAmountHelper {

    @Autowired
    ExchangeCurrencyValueRepository exchangeCurrencyValueRepository;

    public Money convertCurrencyAmount(Money fromCurrencyAmount, Currency toCurrency) {
        if (!Constants.CURRENCIES_SAVED.contains(fromCurrencyAmount.getCurrency().getCurrencyCode())) {
            ExchangeCurrencyValue exchangeCurrencyValue = exchangeCurrencyValueRepository.findByBaseCurrency(Money.DEFAULT_CURRENCY.getCurrencyCode());
            BigDecimal conversionRateToEUR = exchangeCurrencyValue.getConversion_rates().get(fromCurrencyAmount.getCurrency().getCurrencyCode());
            BigDecimal conversionRateToRequiredCurrency = exchangeCurrencyValue.getConversion_rates().get(toCurrency.getCurrencyCode());
            return fromCurrencyAmount.divide(conversionRateToEUR).multiply(conversionRateToRequiredCurrency);
        }
        ExchangeCurrencyValue exchangeCurrencyValue = exchangeCurrencyValueRepository.findByBaseCurrency(fromCurrencyAmount.getCurrency().getCurrencyCode());
        BigDecimal conversionRate = exchangeCurrencyValue.getConversion_rates().get(toCurrency.getCurrencyCode());
        Money moneyConverted = new Money(fromCurrencyAmount);
        moneyConverted.setCurrency(toCurrency);
        moneyConverted = moneyConverted.multiply(conversionRate);
        return moneyConverted;
    }
}
