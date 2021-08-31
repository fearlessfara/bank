package com.bok.bank;

import com.bok.bank.helper.ExchangeCurrencyAmountHelper;
import com.bok.bank.helper.TransactionHelper;
import com.bok.bank.integration.dto.AuthorizationRequestDTO;
import com.bok.bank.integration.dto.AuthorizationResponseDTO;
import com.bok.bank.integration.dto.CardDTO;
import com.bok.bank.integration.dto.TransactionResponseDTO;
import com.bok.bank.integration.dto.WireTransferRequestDTO;
import com.bok.bank.integration.dto.WireTransferResponseDTO;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

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
    TransactionHelper transactionHelper;

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
        Card card = modelTestUtil.createAndSaveActiveCard(user, bankAccount);

        Money moneyToAuthorize = new Money(new BigDecimal(10).setScale(2, RoundingMode.FLOOR), bankAccount.getCurrency());
        AuthorizationResponseDTO checkPaymentAmount = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), null, new com.bok.bank.integration.util.Money(moneyToAuthorize.getCurrency(), moneyToAuthorize.getValue()), "MARKET", card.getToken()));
        Assertions.assertEquals(checkPaymentAmount.reason, "");
        Assertions.assertTrue(checkPaymentAmount.authorized);
        Assertions.assertNotNull(checkPaymentAmount.extTransactionId);
        BankAccount bankAccountAfterAmountAuthorization = bankAccountRepository.findByAccountId(user.getId()).get();
        Money blockedAmountExpected = bankAccount.getBlockedAmount().plus(moneyToAuthorize);
        blockedAmountExpected.setValue(blockedAmountExpected.getValue().setScale(2, RoundingMode.FLOOR));
        Assertions.assertEquals(blockedAmountExpected, bankAccountAfterAmountAuthorization.getBlockedAmount());
        Transaction transaction = transactionRepository.findByPublicId(checkPaymentAmount.extTransactionId.toString()).get();
        Assertions.assertEquals(Transaction.Status.AUTHORISED, transaction.getStatus());
        Assertions.assertEquals(moneyToAuthorize, transaction.getAmount());
        Assertions.assertEquals(bankAccount.getAvailableAmount().subtract(moneyToAuthorize), bankAccountAfterAmountAuthorization.getAvailableAmount());

    }


    @Test
    public void checkPaymentAmountAnotherCurrencyTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        Card card = modelTestUtil.createAndSaveActiveCard(user, bankAccount);

        Money moneyToAuthorize = new Money(new BigDecimal(121).setScale(2, RoundingMode.FLOOR), USD);
        AuthorizationResponseDTO checkPaymentAmount = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new com.bok.bank.integration.util.Money(USD, BigDecimal.valueOf(121)), "MARKET", card.getToken()));
        Assertions.assertEquals(checkPaymentAmount.reason, "");
        Assertions.assertTrue(checkPaymentAmount.authorized);
        Assertions.assertNotNull(checkPaymentAmount.extTransactionId);
        BankAccount bankAccountAfterAmountAuthorization = bankAccountRepository.findByAccountId(user.getId()).get();
        Money blockedAmountExpected = bankAccount.getBlockedAmount().plus(exchangeCurrencyAmountHelper.convertCurrencyAmount(moneyToAuthorize, bankAccount.getCurrency()));
        blockedAmountExpected.setValue(blockedAmountExpected.getValue().setScale(2, RoundingMode.FLOOR));
        Assertions.assertEquals(blockedAmountExpected, bankAccountAfterAmountAuthorization.getBlockedAmount());
        Transaction transaction = transactionRepository.findByPublicId(checkPaymentAmount.extTransactionId.toString()).get();
        Assertions.assertEquals(Transaction.Status.AUTHORISED, transaction.getStatus());
        Money moneyToAuthorizeExpected = exchangeCurrencyAmountHelper.convertCurrencyAmount(moneyToAuthorize, bankAccount.getCurrency());
        moneyToAuthorizeExpected.setValue(moneyToAuthorizeExpected.getValue().setScale(2, RoundingMode.FLOOR));
        Assertions.assertEquals(moneyToAuthorizeExpected, transaction.getAmount());
        Assertions.assertEquals(bankAccount.getAvailableAmount().subtract(moneyToAuthorizeExpected), bankAccountAfterAmountAuthorization.getAvailableAmount());
    }

    @Test
    public void checkInstantWireTransfer() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        User user2 = modelTestUtil.createAndSaveUser(18L);
        BankAccount bankAccount2 = modelTestUtil.createAndSaveBankAccount(user2, Currency.getInstance("USD"));

        com.bok.bank.integration.util.Money moneyToSend = new com.bok.bank.integration.util.Money(USD, BigDecimal.valueOf(12));
        Money moneyToSendBE = new Money(BigDecimal.valueOf(12), USD);
        WireTransferResponseDTO wireTransferResponseDTO = transactionController.wireTransfer(user.getId(), new WireTransferRequestDTO(bankAccount2.getIBAN(), user2.getName() + " " + user2.getSurname(), faker.name().title(), moneyToSend, null, true));
        Assertions.assertEquals(wireTransferResponseDTO.reason, "");
        Assertions.assertTrue(wireTransferResponseDTO.accepted);
        BankAccount bankAccountAfterWireTransfer = bankAccountRepository.findByAccountId(user.getId()).get();
        BankAccount bankAccount2AfterWireTransfer = bankAccountRepository.findByAccountId(user2.getId()).get();
        Money availableAmountBA1 = bankAccount.getAvailableAmount().subtract(exchangeCurrencyAmountHelper.convertCurrencyAmount(moneyToSendBE, bankAccount.getCurrency()));
        availableAmountBA1.setValue(availableAmountBA1.getValue().setScale(2, RoundingMode.FLOOR));
        Assertions.assertEquals(availableAmountBA1, bankAccountAfterWireTransfer.getAvailableAmount());
        Money availableAmountBA2 = bankAccount2.getAvailableAmount().plus(exchangeCurrencyAmountHelper.convertCurrencyAmount(moneyToSendBE, bankAccount2.getCurrency()));
        availableAmountBA2.setValue(availableAmountBA2.getValue().setScale(2, RoundingMode.FLOOR));
        Assertions.assertEquals(availableAmountBA2, bankAccount2AfterWireTransfer.getAvailableAmount());
        Transaction transaction = transactionRepository.findAll().get(0);
        Assertions.assertEquals(Transaction.Status.SETTLED, transaction.getStatus());
    }

    @Test
    public void checkWireTransferAndSettledScheduler() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        User user2 = modelTestUtil.createAndSaveUser(18L);
        BankAccount bankAccount2 = modelTestUtil.createAndSaveBankAccount(user2, Currency.getInstance("USD"));

        com.bok.bank.integration.util.Money moneyToSend = new com.bok.bank.integration.util.Money(USD, BigDecimal.valueOf(12));
        Money moneyToSendBE = new Money(BigDecimal.valueOf(12), USD);
        WireTransferResponseDTO wireTransferResponseDTO = transactionController.wireTransfer(user.getId(), new WireTransferRequestDTO(bankAccount2.getIBAN(), user2.getName() + " " + user2.getSurname(), faker.name().title(), moneyToSend, LocalDate.now().plus(1, ChronoUnit.DAYS), false));
        Assertions.assertEquals(wireTransferResponseDTO.reason, "");
        Assertions.assertTrue(wireTransferResponseDTO.accepted);

        BankAccount bankAccountAfterWireTransfer = bankAccountRepository.findByAccountId(user.getId()).get();
        BankAccount bankAccount2AfterWireTransfer = bankAccountRepository.findByAccountId(user2.getId()).get();
        Money availableAmountBA1 = bankAccount.getAvailableAmount().subtract(exchangeCurrencyAmountHelper.convertCurrencyAmount(moneyToSendBE, bankAccount.getCurrency()));
        availableAmountBA1.setValue(availableAmountBA1.getValue().setScale(2, RoundingMode.FLOOR));
        Assertions.assertEquals(availableAmountBA1, bankAccountAfterWireTransfer.getAvailableAmount());

        Money blockedAmountBA1 = bankAccount.getBlockedAmount().plus(exchangeCurrencyAmountHelper.convertCurrencyAmount(moneyToSendBE, bankAccount.getCurrency()));
        blockedAmountBA1.setValue(blockedAmountBA1.getValue().setScale(2, RoundingMode.FLOOR));
        Assertions.assertEquals(blockedAmountBA1, bankAccountAfterWireTransfer.getBlockedAmount());

        Money availableAmountBA2 = bankAccount2.getAvailableAmount();
        availableAmountBA2.setValue(availableAmountBA2.getValue().setScale(2, RoundingMode.FLOOR));
        Assertions.assertEquals(availableAmountBA2, bankAccount2AfterWireTransfer.getAvailableAmount());
        Transaction transaction = transactionRepository.findAll().get(0);
        Assertions.assertEquals(Transaction.Status.AUTHORISED, transaction.getStatus());
        transaction.setExecutionDate(LocalDate.now());
        transactionRepository.saveAndFlush(transaction);
        transactionHelper.processScheduledTransfers();
        transaction = transactionRepository.findAll().get(0);
        Assertions.assertEquals(Transaction.Status.SETTLED, transaction.getStatus());
    }

    @Test
    public void checkInstantWireTransferToYourselfFAIL() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));

        com.bok.bank.integration.util.Money moneyToSend = new com.bok.bank.integration.util.Money(USD, BigDecimal.valueOf(12));
        Assertions.assertThrows(IllegalStateException.class, () -> transactionController.wireTransfer(user.getId(), new WireTransferRequestDTO(bankAccount.getIBAN(), user.getName() + " " + user.getSurname(), faker.name().title(), moneyToSend, null, true)));
    }

    @Test
    public void checkWireTransferToYourselfFAIL() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        com.bok.bank.integration.util.Money moneyToSend = new com.bok.bank.integration.util.Money(USD, BigDecimal.valueOf(12));
        Assertions.assertThrows(IllegalStateException.class, () -> transactionController.wireTransfer(user.getId(), new WireTransferRequestDTO(bankAccount.getIBAN(), user.getName() + " " + user.getSurname(), faker.name().title(), moneyToSend, LocalDate.now().plus(1, ChronoUnit.DAYS), false)));
    }

    @Test
    public void checkPaymentAmountGreaterWithAnotherCurrencyTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        Card card = modelTestUtil.createAndSaveActiveCard(user, bankAccount);

        AuthorizationResponseDTO checkPaymentAmount = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new com.bok.bank.integration.util.Money(USD, BigDecimal.valueOf(123)), "MARKET", card.getToken()));
        Assertions.assertEquals(checkPaymentAmount.reason, "Amount not available");
        Assertions.assertFalse(checkPaymentAmount.authorized);
        Assertions.assertNotNull(checkPaymentAmount.extTransactionId);
        Transaction transaction = transactionRepository.findByPublicId(checkPaymentAmount.extTransactionId.toString()).get();
        Assertions.assertEquals(Transaction.Status.DECLINED, transaction.getStatus());
    }

    @Test
    public void failCheckPaymentAmountWithNotActiveCardTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        cardController.createCard(user.getId(), new CardDTO(faker.name().title(), DEBIT.name(), faker.name().name()));
        Card card = cardRepository.findAll().get(0);
        AuthorizationResponseDTO checkPaymentAmount = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new com.bok.bank.integration.util.Money(Currency.getInstance("AMD"), BigDecimal.valueOf(64156)), "MARKET", card.getToken()));
        Assertions.assertEquals(checkPaymentAmount.reason, "Card is not ACTIVE, the status is: TO_ACTIVATE");
        Assertions.assertFalse(checkPaymentAmount.authorized);
        Assertions.assertNull(checkPaymentAmount.extTransactionId);
    }

    @Test
    public void checkPaymentAmountWithNotCommonCurrencyTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        Card card = modelTestUtil.createAndSaveActiveCard(user, bankAccount);

        AuthorizationResponseDTO checkPaymentAmount = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new com.bok.bank.integration.util.Money(Currency.getInstance("AMD"), BigDecimal.valueOf(64156)), "MARKET", card.getToken()));
        Assertions.assertEquals(checkPaymentAmount.reason, "Amount not available");
        Assertions.assertFalse(checkPaymentAmount.authorized);
        Assertions.assertNotNull(checkPaymentAmount.extTransactionId);
        Transaction transaction = transactionRepository.findByPublicId(checkPaymentAmount.extTransactionId.toString()).get();
        Assertions.assertEquals(Transaction.Status.DECLINED, transaction.getStatus());
    }

    @Test
    public void listTransactionByCard() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        Card card = modelTestUtil.createAndSaveActiveCard(user, bankAccount);
        Card card2 = modelTestUtil.createAndSaveActiveCard(user, bankAccount);

        AuthorizationResponseDTO firstTransaction = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new com.bok.bank.integration.util.Money(Currency.getInstance("AMD"), BigDecimal.valueOf(64156)), "MARKET", card.getToken()));
        Transaction t1 = transactionRepository.findByPublicId(firstTransaction.extTransactionId.toString()).get();
        AuthorizationResponseDTO secondTransaction = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new com.bok.bank.integration.util.Money(Currency.getInstance("AMD"), BigDecimal.valueOf(64156)), "MARKET", card.getToken()));
        Transaction t2 = transactionRepository.findByPublicId(secondTransaction.extTransactionId.toString()).get();
        AuthorizationResponseDTO thirdTransaction = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new com.bok.bank.integration.util.Money(Currency.getInstance("AMD"), BigDecimal.valueOf(64156)), "MARKET", card.getToken()));
        Transaction t3 = transactionRepository.findByPublicId(thirdTransaction.extTransactionId.toString()).get();
        AuthorizationResponseDTO fourthTransaction = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new com.bok.bank.integration.util.Money(Currency.getInstance("AMD"), BigDecimal.valueOf(64156)), "MARKET", card2.getToken()));
        Transaction t4 = transactionRepository.findByPublicId(fourthTransaction.extTransactionId.toString()).get();

        List<TransactionResponseDTO> card1Transaction = transactionController.getCardTransaction(user.getId(), card.getToken());
        Assertions.assertEquals(card1Transaction.size(), 3);
        card1Transaction.stream().filter(t -> t.publicId.equals(firstTransaction.getExtTransactionId().toString())).forEach(t -> {
            Assertions.assertEquals(t.publicId, t1.getPublicId());
            Assertions.assertEquals(t.type, t1.getType().name());
            Assertions.assertEquals(t.status, t1.getStatus().name());
            Assertions.assertEquals(t.amount.getAmount(), t1.getAmount().getValue().negate());
            Assertions.assertEquals(t.amount.getCurrency(), t1.getAmount().getCurrency());
            Assertions.assertEquals(t.timestamp, t1.getTimestamp());
        });
        card1Transaction.stream().filter(t -> t.publicId.equals(secondTransaction.getExtTransactionId().toString())).forEach(t -> {
            Assertions.assertEquals(t.publicId, t2.getPublicId());
            Assertions.assertEquals(t.type, t2.getType().name());
            Assertions.assertEquals(t.status, t2.getStatus().name());
            Assertions.assertEquals(t.amount.getAmount(), t2.getAmount().getValue().negate());
            Assertions.assertEquals(t.amount.getCurrency(), t2.getAmount().getCurrency());
            Assertions.assertEquals(t.timestamp, t2.getTimestamp());
        });
        card1Transaction.stream().filter(t -> t.publicId.equals(thirdTransaction.getExtTransactionId().toString())).forEach(t -> {
            Assertions.assertEquals(t.publicId, t3.getPublicId());
            Assertions.assertEquals(t.type, t3.getType().name());
            Assertions.assertEquals(t.status, t3.getStatus().name());
            Assertions.assertEquals(t.amount.getAmount(), t3.getAmount().getValue().negate());
            Assertions.assertEquals(t.amount.getCurrency(), t3.getAmount().getCurrency());
            Assertions.assertEquals(t.timestamp, t3.getTimestamp());
        });

        Assertions.assertFalse(card1Transaction.stream().anyMatch(t -> t.publicId.equals(fourthTransaction.getExtTransactionId().toString())));

        List<TransactionResponseDTO> card2Transaction = transactionController.getCardTransaction(user.getId(), card2.getToken());
        Assertions.assertEquals(card2Transaction.size(), 1);

        card2Transaction.stream().filter(t -> t.publicId.equals(fourthTransaction.getExtTransactionId().toString())).forEach(t -> {
            Assertions.assertEquals(t.publicId, t4.getPublicId());
            Assertions.assertEquals(t.type, t4.getType().name());
            Assertions.assertEquals(t.status, t4.getStatus().name());
            Assertions.assertEquals(t.amount.getAmount(), t4.getAmount().getValue().negate());
            Assertions.assertEquals(t.amount.getCurrency(), t4.getAmount().getCurrency());
            Assertions.assertEquals(t.timestamp, t4.getTimestamp());
        });
    }

    @Test
    public void allTransaction() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        Card card = modelTestUtil.createAndSaveActiveCard(user, bankAccount);
        Card card2 = modelTestUtil.createAndSaveActiveCard(user, bankAccount);

        AuthorizationResponseDTO firstTransaction = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new com.bok.bank.integration.util.Money(Currency.getInstance("AMD"), BigDecimal.valueOf(64156)), "MARKET", card.getToken()));
        Transaction t1 = transactionRepository.findByPublicId(firstTransaction.extTransactionId.toString()).get();
        AuthorizationResponseDTO secondTransaction = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new com.bok.bank.integration.util.Money(Currency.getInstance("AMD"), BigDecimal.valueOf(64156)), "MARKET", card.getToken()));
        Transaction t2 = transactionRepository.findByPublicId(secondTransaction.extTransactionId.toString()).get();
        AuthorizationResponseDTO thirdTransaction = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new com.bok.bank.integration.util.Money(Currency.getInstance("AMD"), BigDecimal.valueOf(64156)), "MARKET", card.getToken()));
        Transaction t3 = transactionRepository.findByPublicId(thirdTransaction.extTransactionId.toString()).get();
        AuthorizationResponseDTO fourthTransaction = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new com.bok.bank.integration.util.Money(Currency.getInstance("AMD"), BigDecimal.valueOf(64156)), "MARKET", card2.getToken()));
        Transaction t4 = transactionRepository.findByPublicId(fourthTransaction.extTransactionId.toString()).get();

        List<TransactionResponseDTO> transactions = transactionController.allTransaction(user.getId());
        Assertions.assertEquals(transactions.size(), 4);
        transactions.stream().filter(t -> t.publicId.equals(firstTransaction.getExtTransactionId().toString())).forEach(t -> {
            Assertions.assertEquals(t.publicId, t1.getPublicId());
            Assertions.assertEquals(t.type, t1.getType().name());
            Assertions.assertEquals(t.status, t1.getStatus().name());
            Assertions.assertEquals(t.amount.getAmount(), t1.getAmount().getValue().negate());
            Assertions.assertEquals(t.amount.getCurrency(), t1.getAmount().getCurrency());
            Assertions.assertEquals(t.timestamp, t1.getTimestamp());
        });
        transactions.stream().filter(t -> t.publicId.equals(secondTransaction.getExtTransactionId().toString())).forEach(t -> {
            Assertions.assertEquals(t.publicId, t2.getPublicId());
            Assertions.assertEquals(t.type, t2.getType().name());
            Assertions.assertEquals(t.status, t2.getStatus().name());
            Assertions.assertEquals(t.amount.getAmount(), t2.getAmount().getValue().negate());
            Assertions.assertEquals(t.amount.getCurrency(), t2.getAmount().getCurrency());
            Assertions.assertEquals(t.timestamp, t2.getTimestamp());
        });
        transactions.stream().filter(t -> t.publicId.equals(thirdTransaction.getExtTransactionId().toString())).forEach(t -> {
            Assertions.assertEquals(t.publicId, t3.getPublicId());
            Assertions.assertEquals(t.type, t3.getType().name());
            Assertions.assertEquals(t.status, t3.getStatus().name());
            Assertions.assertEquals(t.amount.getAmount(), t3.getAmount().getValue().negate());
            Assertions.assertEquals(t.amount.getCurrency(), t3.getAmount().getCurrency());
            Assertions.assertEquals(t.timestamp, t3.getTimestamp());
        });

        transactions.stream().filter(t -> t.publicId.equals(fourthTransaction.getExtTransactionId().toString())).forEach(t -> {
            Assertions.assertEquals(t.publicId, t4.getPublicId());
            Assertions.assertEquals(t.type, t4.getType().name());
            Assertions.assertEquals(t.status, t4.getStatus().name());
            Assertions.assertEquals(t.amount.getAmount(), t4.getAmount().getValue().negate());
            Assertions.assertEquals(t.amount.getCurrency(), t4.getAmount().getCurrency());
            Assertions.assertEquals(t.timestamp, t4.getTimestamp());
        });
    }
}
