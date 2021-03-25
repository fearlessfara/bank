package com.bok.bank;

import com.bok.bank.model.ExchangeCurrencyValueHistory;
import com.bok.bank.util.ModelTestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Slf4j
public class ExchangeCurrencyRepositoryTest {

    @Autowired
    ModelTestUtil modelTestUtil;

    @BeforeEach
    public void configureTests() {
        modelTestUtil.clearAll();
        log.info("DB dropped correctly.");
        modelTestUtil.populateDB();
        log.info("DB populated correctly.");
    }

    @Test
    public void findLastValueForAllCurrencyTest(){
        List<ExchangeCurrencyValueHistory> exchangeCurrencyValueHistories = modelTestUtil.exchangeCurrencyValueHistoryRepository.findLastValueForAllCurrency();
        assertThat(exchangeCurrencyValueHistories.get(0).getId()).isEqualTo(1l);
        assertThat(exchangeCurrencyValueHistories.get(1).getId()).isEqualTo(2l);
        assertThat(exchangeCurrencyValueHistories.get(2).getId()).isEqualTo(3l);
        assertThat(exchangeCurrencyValueHistories.get(0).getTime_last_update_unix()).isAfter(exchangeCurrencyValueHistories.get(1).getTime_last_update_unix());
        assertThat(exchangeCurrencyValueHistories.get(1).getTime_last_update_unix()).isAfter(exchangeCurrencyValueHistories.get(2).getTime_last_update_unix());
    }

}
