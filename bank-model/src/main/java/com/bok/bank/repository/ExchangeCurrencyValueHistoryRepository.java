package com.bok.bank.repository;

import com.bok.bank.model.ExchangeCurrencyValueHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExchangeCurrencyValueHistoryRepository extends JpaRepository<ExchangeCurrencyValueHistory, Long> {

    @Query("select ecvh from ExchangeCurrencyValueHistory ecvh ORDER BY ecvh.time_last_update_unix DESC LIMIT 3")
    List<ExchangeCurrencyValueHistory> findLastValueForAllCurrency();

}
