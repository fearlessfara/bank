package com.bok.bank;

import com.bok.bank.helper.ExchangeCurrencyAmountHelper;
import com.bok.bank.integration.dto.CardDTO;
import com.bok.bank.integration.service.AccountController;
import com.bok.bank.integration.service.CardController;
import com.bok.bank.integration.service.TransactionController;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.User;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.repository.CardRepository;
import com.bok.bank.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

import java.util.Currency;

import static com.bok.bank.ModelTestUtil.faker;
import static com.bok.bank.model.Card.Type.DEBIT;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
@PropertySource(ignoreResourceNotFound = true, value = "classpath:resources.application-test.yml")
public class CardControllerTest {

    @Autowired
    ModelTestUtil modelTestUtil;

    @Autowired
    AccountController accountController;

    @Autowired
    TransactionController transactionController;

    @Autowired
    CardController cardController;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    ExchangeCurrencyAmountHelper exchangeCurrencyAmountHelper;

    @Autowired
    TransactionRepository transactionRepository;

    @BeforeEach
    public void init() {
        modelTestUtil.clearAll();
    }
    @Test
    public void createCardTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        cardController.createCard(user.getId(), new CardDTO(faker.name().title(), DEBIT.name(), faker.name().name()));

    }
}
