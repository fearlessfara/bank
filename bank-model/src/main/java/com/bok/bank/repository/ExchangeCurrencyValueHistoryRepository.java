package com.bok.bank.repository;

import com.bok.bank.model.ExchangeCurrencyValueHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExchangeCurrencyValueHistoryRepository extends JpaRepository<ExchangeCurrencyValueHistory, Long> {

    @Query(value = "select * " +
            "from bank.exchange_currency_value_history ecvh " +
            "where ecvh.time_next_update_unix = " +
            "      (select max(ecvh2.time_next_update_unix) " +
            "       from exchange_currency_value_history ecvh2 " +
            "       where ecvh.base_currency = ecvh2.base_currency)", nativeQuery = true)
//    @Query("select ExchangeCurrencyValueHistory from ExchangeCurrencyValueHistory ecvh where ecvh.time_next_update_unix = " +
//            "(select max(ecvh2.time_next_update_unix) " +
//            "from ExchangeCurrencyValueHistory ecvh2 where ecvh.baseCurrency = ecvh2.baseCurrency)")
    List<ExchangeCurrencyValueHistory> findLastValueForAllCurrency();

}
