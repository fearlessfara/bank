package com.bok.bank.helper;

import com.bok.bank.exception.AccountException;
import com.bok.bank.exception.BankAccountException;
import com.bok.bank.exception.CardException;
import com.bok.bank.integration.dto.CardDTO;
import com.bok.bank.integration.dto.CardInfoDTO;
import com.bok.bank.model.Account;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.Card;
import com.bok.bank.model.ConfirmationEmailHistory;
import com.bok.bank.repository.AccountRepository;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.repository.CardRepository;
import com.bok.bank.util.Constants;
import com.bok.bank.util.CreditCardNumberGenerator;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CardHelper {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    CreditCardNumberGenerator creditCardNumberGenerator;

    @Autowired
    EmailHelper emailHelper;

    @Autowired
    ConfirmationEmailHelper confirmationEmailHelper;

    @Autowired
    AccountRepository accountRepository;

    public List<CardInfoDTO> getAllCards(Long accountId) {
        List<CardRepository.Projection.CardInfo> cardInfos = cardRepository.findAllCardInfoByAccountId(accountId);
        return cardInfos.stream()
                .map(cardInfo -> new CardInfoDTO(cardInfo.getCardId(), cardInfo.getName(), cardInfo.getCardStatus().name(), cardInfo.getType().name(), cardInfo.getLabel(), creditCardNumberGenerator.maskingPan(cardInfo.getMaskedPan())))
                .collect(Collectors.toList());
    }

    public CardInfoDTO getCardInfo(Long accountId, Long cardId) {
        CardRepository.Projection.CardInfo cardInfo = cardRepository.findCardInfoByAccountIdAndCardId(accountId, cardId).orElseThrow(() -> new IllegalArgumentException("Card not found for cardId " + cardId + "and accountId " + accountId));
        return new CardInfoDTO(cardInfo.getCardId(), cardInfo.getName(), cardInfo.getCardStatus().name(), cardInfo.getType().name(), cardInfo.getLabel(), creditCardNumberGenerator.maskingPan(cardInfo.getMaskedPan()));
    }

    public String getPlainPan(Long accountId, Long cardId) {
        return cardRepository.findPlainPanByAccountIdAndCardId(accountId, cardId).orElseThrow(() -> new IllegalArgumentException("Card not found for cardId: " + cardId + " and accountId: " + accountId));
    }

    public String getCardToken(Long accountId, Long cardId) {
        return cardRepository.findCardTokenByAccountIdAndCardId(accountId, cardId).orElseThrow(() -> new IllegalArgumentException("Card not found for cardId: " + cardId + " and accountId: " + accountId));
    }

    public int getCvv(Long accountId, Long cardId) {
        return cardRepository.findCvvByAccountIdAndCardId(accountId, cardId).orElseThrow(() -> new IllegalArgumentException("Card not found for cardId: " + cardId + " and accountId: " + accountId));
    }

    public String getPIN(Long accountId, Long cardId) {
        Account account = accountRepository.findById(accountId).orElseThrow(AccountException::new);
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new IllegalArgumentException("Card not found for cardId: " + cardId));
        Preconditions.checkArgument(card.getAccount().getId().equals(accountId), "Card not found in account: " + account.getName());
        return emailHelper.sendPINEmail(account, card);
    }

    public String createCard(Long accountId, CardDTO cardDTO) {
        Account account = accountRepository.findById(accountId).orElseThrow(AccountException::new);
        BankAccountRepository.Projections.BankAccountBasicInfo bankAccountBasicInfo = bankAccountRepository.findBankAccountBasicInfoByAccountId(accountId).orElseThrow(BankAccountException::new);
        Preconditions.checkArgument(bankAccountBasicInfo.getStatus().equals(BankAccount.Status.ACTIVE), "Fail creation card related to not active bank account, bankAccountId: " + bankAccountBasicInfo.getId());
        Card newCard = new Card(cardDTO.name, new Account(accountId), Card.CardStatus.TO_ACTIVATE, Card.Type.valueOf(cardDTO.type.trim()), Instant.now().plus(Period.ofYears(4).getDays(), ChronoUnit.DAYS),
                creditCardNumberGenerator.generateToken(), cardDTO.label, creditCardNumberGenerator.generate(Constants.BIN_BOK, 15), new BankAccount(bankAccountBasicInfo.getId()), creditCardNumberGenerator.generateCvv(), creditCardNumberGenerator.generatePIN());
        newCard = cardRepository.save(newCard);
        emailHelper.sendCardConfirmationEmail(account, newCard);
        return "Please check your mail and active your new card";
    }

    public void checkNewCardDTOData(CardDTO cardDTO) {
        Preconditions.checkArgument(cardDTO.name.trim().length() > 1, "Invalid card name : " + cardDTO.name.trim());
        Preconditions.checkArgument(cardDTO.type.trim().equals(Card.Type.CREDIT.name()) || cardDTO.type.trim().equals(Card.Type.DEBIT.name()) || cardDTO.type.trim().equals(Card.Type.VIRTUAL.name()) || cardDTO.type.trim().equals(Card.Type.ONE_USE.name()), "Invalid card type : " + cardDTO.type.trim());
    }

    public void checkPin(Long accountId, Long cardId, String PIN) {
        Preconditions.checkNotNull(PIN, "PIN must not be null");
        Card card = cardRepository.findById(cardId).orElseThrow(CardException::new);
        Preconditions.checkArgument(card.getAccount().getId().equals(accountId), "Card selected not is in your account");
        Preconditions.checkArgument(card.getPIN().equals(PIN), "PIN not valid");
    }

    @Scheduled(fixedDelay = 31600000, initialDelay = 1000)
    public void checkCardExpired() {
        List<Card> cards = cardRepository.findAll();
        if(CollectionUtils.isEmpty(cards)){
            return;
        }
        List<Card> cardsExpired = new ArrayList<>();
        cards.stream().filter(card -> card.getExpirationDate().isAfter(Instant.now())).forEach(card -> {
            card.setCardStatus(Card.CardStatus.EXPIRED);
            cardsExpired.add(card);
        });
        cardRepository.saveAll(cardsExpired);
    }

    public String changeCardStatus(Long cardId, Card.CardStatus status) {
        Card card = cardRepository.findById(cardId).orElseThrow(CardException::new);
        card.setCardStatus(status);
        cardRepository.saveAndFlush(card);
        return "The status of the card: " + card.getName() + ", now is " + status.name();
    }
}
