package com.bok.bank.integration;

import com.bok.bank.integration.model.ExchangeCurrencyValueHistory;
import com.bok.bank.integration.repository.ExchangeCurrencyValueHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan(basePackages = "com.bok.bank")
@EnableAutoConfiguration
@Slf4j
public class ExchangeCurrencyRepositoryTest {

    @Autowired
    ModelTestUtil modelTestUtil;

    @Autowired
    ExchangeCurrencyValueHistoryRepository exchangeCurrencyValueHistoryRepository;

    @Before
    public void configureTests() {
        modelTestUtil.clearAll();
        log.info("DB dropped correctly.");
        modelTestUtil.populateDB();
        log.info("DB populated correctly.");
    }

    @Test
    public void findLastValueForAllCurrencyTest() {
        List<ExchangeCurrencyValueHistory> exchangeCurrencyValueHistories = exchangeCurrencyValueHistoryRepository.findLastValueForAllCurrency();
        assertThat(exchangeCurrencyValueHistories.get(0).getId()).isEqualTo(1l);
        assertThat(exchangeCurrencyValueHistories.get(1).getId()).isEqualTo(2l);
        assertThat(exchangeCurrencyValueHistories.get(2).getId()).isEqualTo(3l);
        assertThat(exchangeCurrencyValueHistories.get(0).getTime_last_update_unix()).isAfter(exchangeCurrencyValueHistories.get(1).getTime_last_update_unix());
        assertThat(exchangeCurrencyValueHistories.get(1).getTime_last_update_unix()).isAfter(exchangeCurrencyValueHistories.get(2).getTime_last_update_unix());
    }

}
