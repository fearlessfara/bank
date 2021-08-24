package com.bok.bank;

import com.bok.bank.helper.ExchangeCurrencyAmountHelper;
import com.bok.bank.integration.dto.AuthorizationRequestDTO;
import com.bok.bank.integration.dto.AuthorizationResponseDTO;
import com.bok.bank.integration.dto.CardDTO;
import com.bok.bank.integration.service.AccountController;
import com.bok.bank.integration.service.CardController;
import com.bok.bank.integration.service.TransactionController;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.Card;
import com.bok.bank.model.Transaction;
import com.bok.bank.model.User;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.repository.CardRepository;
import com.bok.bank.repository.TransactionRepository;
import com.bok.bank.util.Money;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.UUID;

import static com.bok.bank.ModelTestUtil.EUR;
import static com.bok.bank.ModelTestUtil.USD;
import static com.bok.bank.ModelTestUtil.faker;
import static com.bok.bank.model.Card.Type.DEBIT;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
@PropertySource(ignoreResourceNotFound = true, value = "classpath:resources.application-test.yml")
public class TransactionControllerTest {

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
    public void checkPaymentAmountTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user);
        cardController.createCard(user.getId(), new CardDTO(faker.name().title(), DEBIT.name(), faker.name().name()));
        Card card = cardRepository.findAll().get(0);

        Money moneyToAuthorize = new Money(new BigDecimal(10).setScale(2,RoundingMode.FLOOR), bankAccount.getCurrency());
        AuthorizationResponseDTO checkPaymentAmount = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), null, new com.bok.bank.integration.util.Money(moneyToAuthorize.getCurrency(), moneyToAuthorize.getValue()), "MARKET", card.getToken()));
        Assertions.assertEquals(checkPaymentAmount.reason, "");
        Assertions.assertTrue(checkPaymentAmount.authorized);
        Assertions.assertNotNull(checkPaymentAmount.extTransactionId);
        BankAccount bankAccountAfterAmountAuthorization = bankAccountRepository.findByAccountId(user.getId()).get();
        Money blockedAmountExpected = bankAccount.getBlockedAmount().plus(moneyToAuthorize);
        blockedAmountExpected.setValue(blockedAmountExpected.getValue().setScale(2, RoundingMode.FLOOR));
        Assertions.assertEquals(blockedAmountExpected, bankAccountAfterAmountAuthorization.getBlockedAmount());
        Transaction transaction = transactionRepository.findByPublicId(checkPaymentAmount.extTransactionId).get();
        Assertions.assertEquals(Transaction.Status.AUTHORISED, transaction.getStatus());
        Assertions.assertEquals(moneyToAuthorize, transaction.getAmount());
        Assertions.assertEquals(bankAccount.getAvailableAmount().subtract(moneyToAuthorize), bankAccountAfterAmountAuthorization.getAvailableAmount());

    }

    @Test
    public void checkPaymentAmountFailTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        cardController.createCard(user.getId(), new CardDTO(faker.name().title(), DEBIT.name(), faker.name().name()));
        Card card = cardRepository.findAll().get(0);

        AuthorizationResponseDTO checkPaymentAmount = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new com.bok.bank.integration.util.Money(EUR, BigDecimal.valueOf(50)), "MARKET", card.getToken()));
        Assertions.assertEquals(checkPaymentAmount.reason, "User not have a bank account or is bank account not is active");
        Assertions.assertFalse(checkPaymentAmount.authorized);
    }

    @Test
    public void checkPaymentAmountAnotherCurrencyTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        cardController.createCard(user.getId(), new CardDTO(faker.name().title(), DEBIT.name(), faker.name().name()));
        Card card = cardRepository.findAll().get(0);
        Money moneyToAuthorize = new Money(new BigDecimal(121).setScale(2,RoundingMode.FLOOR), USD);
        AuthorizationResponseDTO checkPaymentAmount = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new com.bok.bank.integration.util.Money(USD, BigDecimal.valueOf(121)), "MARKET", card.getToken()));
        Assertions.assertEquals(checkPaymentAmount.reason, "");
        Assertions.assertTrue(checkPaymentAmount.authorized);
        Assertions.assertNotNull(checkPaymentAmount.extTransactionId);
        BankAccount bankAccountAfterAmountAuthorization = bankAccountRepository.findByAccountId(user.getId()).get();
        Money blockedAmountExpected = bankAccount.getBlockedAmount().plus(exchangeCurrencyAmountHelper.convertCurrencyAmount(moneyToAuthorize, bankAccount.getCurrency()));
        blockedAmountExpected.setValue(blockedAmountExpected.getValue().setScale(2, RoundingMode.FLOOR));
        Assertions.assertEquals(blockedAmountExpected, bankAccountAfterAmountAuthorization.getBlockedAmount());
        Transaction transaction = transactionRepository.findByPublicId(checkPaymentAmount.extTransactionId).get();
        Assertions.assertEquals(Transaction.Status.AUTHORISED, transaction.getStatus());
        Money moneyToAuthorizeExpected = exchangeCurrencyAmountHelper.convertCurrencyAmount(moneyToAuthorize, bankAccount.getCurrency());
        moneyToAuthorizeExpected.setValue(moneyToAuthorizeExpected.getValue().setScale(2, RoundingMode.FLOOR));
        Assertions.assertEquals(moneyToAuthorizeExpected, transaction.getAmount());
        Assertions.assertEquals(bankAccount.getAvailableAmount().subtract(moneyToAuthorizeExpected), bankAccountAfterAmountAuthorization.getAvailableAmount());
    }

    @Test
    public void checkPaymentAmountGreaterWithAnotherCurrencyTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        cardController.createCard(user.getId(), new CardDTO(faker.name().title(), DEBIT.name(), faker.name().name()));
        Card card = cardRepository.findAll().get(0);
        AuthorizationResponseDTO checkPaymentAmount = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new com.bok.bank.integration.util.Money(USD, BigDecimal.valueOf(123)), "MARKET", card.getToken()));
        Assertions.assertEquals(checkPaymentAmount.reason, "Amount not available");
        Assertions.assertFalse(checkPaymentAmount.authorized);
        Assertions.assertNotNull(checkPaymentAmount.extTransactionId);
        Transaction transaction = transactionRepository.findByPublicId(checkPaymentAmount.extTransactionId).get();
        Assertions.assertEquals(Transaction.Status.DECLINED, transaction.getStatus());
    }

    @Test
    public void checkPaymentAmountWithNotCommonCurrencyTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        cardController.createCard(user.getId(), new CardDTO(faker.name().title(), DEBIT.name(), faker.name().name()));
        Card card = cardRepository.findAll().get(0);
        AuthorizationResponseDTO checkPaymentAmount = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new com.bok.bank.integration.util.Money(Currency.getInstance("AMD"), BigDecimal.valueOf(64156)), "MARKET", card.getToken()));
        Assertions.assertEquals(checkPaymentAmount.reason, "Amount not available");
        Assertions.assertFalse(checkPaymentAmount.authorized);
        Assertions.assertNotNull(checkPaymentAmount.extTransactionId);
        Transaction transaction = transactionRepository.findByPublicId(checkPaymentAmount.extTransactionId).get();
        Assertions.assertEquals(Transaction.Status.DECLINED, transaction.getStatus());
    }

    @Test
    public void listTransactionByCard() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        cardController.createCard(user.getId(), new CardDTO(faker.name().title(), DEBIT.name(), faker.name().name()));
        Card card = cardRepository.findAll().get(0);
        AuthorizationResponseDTO firstTransaction = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new com.bok.bank.integration.util.Money(Currency.getInstance("AMD"), BigDecimal.valueOf(64156)), "MARKET", card.getToken()));
        AuthorizationResponseDTO secondTransaction = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new com.bok.bank.integration.util.Money(Currency.getInstance("AMD"), BigDecimal.valueOf(64156)), "MARKET", card.getToken()));
        AuthorizationResponseDTO thirdTransaction = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new com.bok.bank.integration.util.Money(Currency.getInstance("AMD"), BigDecimal.valueOf(64156)), "MARKET", card.getToken()));

    }
}
