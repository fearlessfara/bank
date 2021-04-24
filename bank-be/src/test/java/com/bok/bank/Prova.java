package com.bok.bank;

import com.bok.bank.repository.CardRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class Prova {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ModelTestUtil modelTestUtil;

    @Before
    public void configureTests() {
        modelTestUtil.clearAll();
        log.info("DB dropped correctly.");
        modelTestUtil.populateDB();
        log.info("DB populated correctly.");
    }

    @Test
    public void queryTest(){
        assertEquals(cardRepository.findAll().size(), 2);
    }

}
