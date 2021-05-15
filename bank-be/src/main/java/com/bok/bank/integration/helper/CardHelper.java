package com.bok.bank.integration.helper;

import com.bok.bank.integration.dto.CardInfoDTO;
import com.bok.bank.integration.dto.CardDTO;
import com.bok.bank.integration.model.Account;
import com.bok.bank.integration.model.BankAccount;
import com.bok.bank.integration.model.Card;
import com.bok.bank.integration.model.ConfirmationEmailHistory;
import com.bok.bank.integration.repository.AccountRepository;
import com.bok.bank.integration.repository.BankAccountRepository;
import com.bok.bank.integration.repository.CardRepository;
import com.bok.bank.integration.util.Constants;
import com.bok.bank.integration.util.CreditCardNumberGenerator;
import com.bok.bank.integration.util.exception.AccountException;
import com.bok.bank.integration.util.exception.BankAccountException;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;
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

    public int getCvv(Long accountId, Long cardId) {
        return cardRepository.findCvvByAccountIdAndCardId(accountId, cardId).orElseThrow(() -> new IllegalArgumentException("Card not found for cardId: " + cardId + " and accountId: " + accountId));
    }

    public String createCard(Long accountId, CardDTO cardDTO) {
        Account account = accountRepository.findById(accountId).orElseThrow(AccountException::new);
        BankAccountRepository.Projections.BankAccountBasicInfo bankAccountBasicInfo = bankAccountRepository.findBankAccountBasicInfoByAccountId(accountId).orElseThrow(BankAccountException::new);
        Preconditions.checkArgument(bankAccountBasicInfo.getStatus().equals(BankAccount.Status.ACTIVE), "Fail creation card related to not active bank account, bankAccountId: " + bankAccountBasicInfo.getId());
        Card newCard = new Card(cardDTO.name, new Account(accountId), Card.CardStatus.TO_ACTIVATE, Card.Type.valueOf(cardDTO.type.trim()), Instant.now().plus(Period.ofYears(4).getDays(), ChronoUnit.DAYS),
                creditCardNumberGenerator.generateToken(), cardDTO.label, creditCardNumberGenerator.generate(Constants.BIN_BOK, 15), new BankAccount(bankAccountBasicInfo.getId()), creditCardNumberGenerator.generateCvv());
        newCard = cardRepository.save(newCard);
        emailHelper.sendCardConfirmationEmail(account, newCard);
        return "Please check your mail and active your new card";
    }

    public CardInfoDTO verifyCard(Long accountId, String confirmationToken) {
        ConfirmationEmailHistory confirmationEmailHistory = confirmationEmailHelper.findAndVerifyConfirmationToken(accountId, confirmationToken, ConfirmationEmailHistory.ResourceType.CARD);
        Preconditions.checkArgument(cardRepository.changeCardStatus(confirmationEmailHistory.getResourceId(), Card.CardStatus.ACTIVE) < 0, "Card activation is failed, please try again");
        return getCardInfo(accountId, confirmationEmailHistory.getResourceId());
    }

    public void checkNewCardDTOData(CardDTO cardDTO) {
        Preconditions.checkArgument(cardDTO.name.trim().length() > 1, "Invalid card name : " + cardDTO.name.trim());
        Preconditions.checkArgument(cardDTO.type.trim().equals(Card.Type.CREDIT.name()) || cardDTO.type.trim().equals(Card.Type.DEBIT.name()) || cardDTO.type.trim().equals(Card.Type.VIRTUAL.name()) || cardDTO.type.trim().equals(Card.Type.ONE_USE.name()), "Invalid card type : " + cardDTO.type.trim());
    }
}
