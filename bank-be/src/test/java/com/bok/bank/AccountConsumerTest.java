package com.bok.bank;

import com.bok.bank.helper.AccountHelper;
import com.bok.bank.messaging.AccountConsumer;
import com.bok.bank.model.User;
import com.bok.bank.producer.Producer;
import com.bok.bank.repository.CardRepository;
import com.bok.parent.integration.message.AccountCreationMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
public class AccountConsumerTest {

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

    @BeforeEach
    public void configureTests() {
        modelTestUtil.clearAll();
        log.info("DB dropped correctly.");
        modelTestUtil.populateDB();
        log.info("DB populated correctly.");
    }

    @Test
    public void creationUserTest() throws InterruptedException {
        producer.send(new AccountCreationMessage("Domenico", "", "Fasano", "mico@gmail.com", new Date(10212541), "Fasano", "Italia", false, "FSNDMC99C13D508Y", "", "+39", "3926772950", "23", "via le mani dal naso", "Locorotondo", "BA", "Italy", "70010", 123L, User.Gender.M.name()));
        TimeUnit.SECONDS.sleep(5);
        assertTrue(accountHelper.getAccountInfo(123L) != null);
    }

    @Test
    public void queryTest() {
        assertEquals(cardRepository.findAll().size(), 2);
    }

}
