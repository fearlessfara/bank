package com.bok.bank.integration.repository;

import com.bok.bank.integration.model.ExchangeCurrencyValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeCurrencyValueRepository extends JpaRepository<ExchangeCurrencyValue, Long> {

    ExchangeCurrencyValue findByBaseCurrency(String baseCurrency);
}
