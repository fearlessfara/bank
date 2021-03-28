package com.bok.bank.repository;

import com.bok.bank.model.ExchangeCurrencyValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeCurrencyValueRepository extends JpaRepository<ExchangeCurrencyValue, Long> {

    ExchangeCurrencyValue findByBaseCurrency(String baseCurrency);
}
