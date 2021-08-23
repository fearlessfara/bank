package com.bok.bank;

import com.bok.bank.helper.AccountHelper;
import com.bok.bank.integration.dto.AccountInfoDTO;
import com.bok.bank.messaging.AccountConsumer;
import com.bok.bank.model.Account;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.User;
import com.bok.bank.producer.Producer;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.repository.CardRepository;
import com.bok.parent.integration.message.AccountCreationMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.bok.bank.model.Account.Status.ACTIVE;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
public class AccountConsumerTest {

    @Autowired
    BankAccountRepository bankAccountRepository;

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
    public void creationAccountCompleteTest() throws InterruptedException {
        AccountCreationMessage accountCreationMessage = new AccountCreationMessage("Domenico", "", "Fasano", "mico@gmail.com", new Date(10212541), "Fasano", "Italia", false, "FSNDMC99C13D508Y", "", "+39", "3926772950", "23", "via le mani dal naso", "Locorotondo", "BA", "Italy", "70010", 123L, User.Gender.M.name());
        producer.send(accountCreationMessage);
        TimeUnit.SECONDS.sleep(5);
        AccountInfoDTO accountInfo = accountHelper.getAccountInfo(123L);
        Assertions.assertEquals(accountInfo.email, accountCreationMessage.email);
        Assertions.assertEquals(accountInfo.fullName, accountCreationMessage.name + " " + accountCreationMessage.surname);
        Assertions.assertEquals(accountInfo.mobile, accountCreationMessage.mobile);
        Assertions.assertEquals(accountInfo.type, Account.Type.INDIVIDUAL_USER.name());
        Assertions.assertEquals(accountInfo.status, ACTIVE.name());
        BankAccount bankAccount = bankAccountRepository.findByAccountId(123L).get();
        Assertions.assertNotNull(bankAccount);
        Assertions.assertEquals(bankAccount.getAvailableAmount().getValue(), BigDecimal.valueOf(100000));
    }


}
