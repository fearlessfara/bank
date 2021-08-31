package com.bok.bank;

import com.bok.bank.helper.CardHelper;
import com.bok.bank.helper.ExchangeCurrencyAmountHelper;
import com.bok.bank.integration.dto.CardDTO;
import com.bok.bank.integration.dto.CardInfoDTO;
import com.bok.bank.integration.dto.PinDTO;
import com.bok.bank.integration.service.AccountController;
import com.bok.bank.integration.service.CardController;
import com.bok.bank.integration.service.TransactionController;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.Card;
import com.bok.bank.model.User;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.repository.CardRepository;
import com.bok.bank.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import static com.bok.bank.ModelTestUtil.faker;
import static com.bok.bank.model.Card.CardStatus.ACTIVE;
import static com.bok.bank.model.Card.CardStatus.BROKEN;
import static com.bok.bank.model.Card.CardStatus.DEACTIVATED;
import static com.bok.bank.model.Card.CardStatus.DESTROYED;
import static com.bok.bank.model.Card.CardStatus.EXPIRED;
import static com.bok.bank.model.Card.CardStatus.LOST;
import static com.bok.bank.model.Card.CardStatus.STOLEN;
import static com.bok.bank.model.Card.CardStatus.TO_ACTIVATE;
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
    CardHelper cardHelper;

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
        modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        CardDTO newCard = new CardDTO(faker.name().title(), DEBIT.name(), faker.name().name());
        String result = cardController.createCard(user.getId(), newCard);
        Assertions.assertEquals("Please check your mail and active your new card", result);
        Card card = cardRepository.findAll().get(0);
        Assertions.assertNotNull(card);
        Assertions.assertEquals(newCard.name, card.getName());
        Assertions.assertEquals(newCard.label, card.getLabel());
        Assertions.assertEquals(newCard.type, card.getType().name());
        Assertions.assertEquals(TO_ACTIVATE, card.getCardStatus());
        Assertions.assertEquals(16, card.getMaskedPan().length());
        Assertions.assertEquals(3, (card.getCvv()+"").length());
        Assertions.assertEquals(5, card.getPIN().length());
        Assertions.assertEquals(user.getId(), card.getAccount().getId());
    }

    @Test
    public void activationCardTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        CardDTO newCard = new CardDTO(faker.name().title(), DEBIT.name(), faker.name().name());
        cardController.createCard(user.getId(), newCard);
        Card card = cardRepository.findAll().get(0);
        cardController.activation(user.getId(), card.getId(), new PinDTO(card.getPIN()));
        card = cardRepository.findById(card.getId()).get();
        Assertions.assertEquals(ACTIVE, card.getCardStatus());
    }

    @Test
    public void deleteCardTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        CardDTO newCard = new CardDTO(faker.name().title(), DEBIT.name(), faker.name().name());
        cardController.createCard(user.getId(), newCard);
        Card card = cardRepository.findAll().get(0);
        cardController.delete(user.getId(), card.getId(), new PinDTO(card.getPIN()));
        card = cardRepository.findById(card.getId()).get();
        Assertions.assertEquals(DESTROYED, card.getCardStatus());
    }

    @Test
    public void changeCardStatusTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        CardDTO newCard = new CardDTO(faker.name().title(), DEBIT.name(), faker.name().name());
        cardController.createCard(user.getId(), newCard);
        Card card = cardRepository.findAll().get(0);

        Card.CardStatus cardStatus = LOST;
        cardController.changeCardStatus(user.getId(), card.getId(), cardStatus.name(), new PinDTO(card.getPIN()));
        card = cardRepository.findById(card.getId()).get();
        Assertions.assertEquals(cardStatus, card.getCardStatus());

        cardStatus = DEACTIVATED;
        cardController.changeCardStatus(user.getId(), card.getId(), cardStatus.name(), new PinDTO(card.getPIN()));
        card = cardRepository.findById(card.getId()).get();
        Assertions.assertEquals(cardStatus, card.getCardStatus());

        cardStatus = EXPIRED;
        Card finalCard = card;
        Card.CardStatus finalCardStatus = cardStatus;
        Assertions.assertThrows(IllegalArgumentException.class, () -> cardController.changeCardStatus(user.getId(), finalCard.getId(), finalCardStatus.name(), new PinDTO(finalCard.getPIN())));

        cardStatus = ACTIVE;
        Card.CardStatus finalCardStatus1 = cardStatus;
        Assertions.assertThrows(IllegalArgumentException.class, () -> cardController.changeCardStatus(user.getId(), finalCard.getId(), finalCardStatus1.name(), new PinDTO(finalCard.getPIN())));

        cardStatus = DESTROYED;
        Card.CardStatus finalCardStatus2 = cardStatus;
        Assertions.assertThrows(IllegalArgumentException.class, () -> cardController.changeCardStatus(user.getId(), finalCard.getId(), finalCardStatus2.name(), new PinDTO(finalCard.getPIN())));


        cardStatus = STOLEN;
        cardController.changeCardStatus(user.getId(), card.getId(), cardStatus.name(), new PinDTO(card.getPIN()));
        card = cardRepository.findById(card.getId()).get();
        Assertions.assertEquals(cardStatus, card.getCardStatus());

        card.setCardStatus(ACTIVE);
        card = cardRepository.saveAndFlush(card);
        cardStatus = BROKEN;
        cardController.changeCardStatus(user.getId(), card.getId(), cardStatus.name(), new PinDTO(card.getPIN()));
        card = cardRepository.findById(card.getId()).get();
        Assertions.assertEquals(cardStatus, card.getCardStatus());
    }

    @Test
    public void getPIN_PAN_CvvAndToken_Test() {
        User user = modelTestUtil.createAndSaveUser(17L);
        modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        CardDTO newCard = new CardDTO(faker.name().title(), DEBIT.name(), faker.name().name());
        cardController.createCard(user.getId(), newCard);
        Card card = cardRepository.findAll().get(0);
        Assertions.assertEquals("Please check your email, the PIN has been send", cardController.getPIN(user.getId(), card.getId()));
        Assertions.assertEquals(card.getToken(), cardController.getCardToken(user.getId(), card.getId()));
        Assertions.assertEquals(card.getCvv(), cardController.getCvv(user.getId(), card.getId(), new PinDTO(card.getPIN())));
        Assertions.assertEquals(card.getMaskedPan(), cardController.getPlainPan(user.getId(), card.getId(), new PinDTO(card.getPIN())));
    }

    @Test
    public void findCardAndAllCardsTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        Card card = modelTestUtil.createAndSaveActiveCard(user, bankAccount);
        CardInfoDTO cardInfoDTO = cardController.findCard(user.getId(), card.getId());
        Assertions.assertEquals(card.getName(), cardInfoDTO.name);
        Assertions.assertEquals(card.getLabel(), cardInfoDTO.label);
        Assertions.assertEquals(card.getType().name(), cardInfoDTO.type);

        User user2 = modelTestUtil.createAndSaveUser(18L);
        BankAccount bankAccount2 = modelTestUtil.createAndSaveBankAccount(user2, Currency.getInstance("EUR"));
        Card card2 = modelTestUtil.createAndSaveActiveCard(user2, bankAccount2);
        Card card3 = modelTestUtil.createAndSaveActiveCard(user2, bankAccount2);
        Card card4 = modelTestUtil.createAndSaveActiveCard(user2, bankAccount2);
        List<CardInfoDTO> cardInfoDTOS = cardController.allCards(user2.getId());
        List<Long> cardIdsFound = cardInfoDTOS.stream().map(CardInfoDTO::getCardId).collect(Collectors.toList());
        Assertions.assertTrue(cardIdsFound.contains(card2.getId()));
        Assertions.assertTrue(cardIdsFound.contains(card3.getId()));
        Assertions.assertTrue(cardIdsFound.contains(card4.getId()));
        Assertions.assertFalse(cardIdsFound.contains(card.getId()));
    }

    @Test
    public void expirationCardSchedulerTest() throws InterruptedException {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        Card card = modelTestUtil.createAndSaveActiveCard(user, bankAccount);
        card.setExpirationDate(Instant.now().plus(Period.ofDays(4).getDays(), ChronoUnit.DAYS));
        cardRepository.saveAndFlush(card);
        Thread.sleep(700);
        cardHelper.checkCardExpired();
        Assertions.assertEquals(EXPIRED, cardRepository.findById(card.getId()).get().getCardStatus());
    }

}
