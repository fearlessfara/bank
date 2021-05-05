package com.bok.bank;

import com.bok.bank.consumer.AccountConsumer;
import com.bok.bank.helper.AccountHelper;
import com.bok.bank.model.User;
import com.bok.bank.producer.Producer;
import com.bok.bank.repository.CardRepository;
import com.bok.integration.AccountCreationMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.time.LocalDate;

import static junit.framework.TestCase.assertEquals;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
public class Prova {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ModelTestUtil modelTestUtil;

    @Autowired
    AccountConsumer accountConsumer;

    @Autowired
    AccountHelper accountHelper;

    @Autowired
    Producer producer;

    @Before
    public void configureTests() {
        modelTestUtil.clearAll();
        log.info("DB dropped correctly.");
        modelTestUtil.populateDB();
        log.info("DB populated correctly.");
    }

    @Test
    public void creationUserTest() throws InterruptedException {
        producer.send(new AccountCreationMessage("Domenico", "", "Fasano", "mico.fasano@gmail.com", Date.valueOf(LocalDate.of(1999, 3, 13)), "Fasano", "Italia", false, "FSNDNC99C13D508Y", "", "+39", "3926772950", "23", "via le mani dal naso", "Locorotondo", "BA", "Italy", "70010", 123L, User.Gender.M.name()));
        wait(1000);
        log.info(accountHelper.getAccountInfo(123L).toString());
    }

    @Test
    public void queryTest(){
        assertEquals(cardRepository.findAll().size(), 2);
    }

}
