package com.bok.bank.helper;

import com.bok.bank.dto.CardInfoDTO;
import com.bok.bank.dto.CardDTO;
import com.bok.bank.model.Account;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.Card;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.repository.CardRepository;
import com.bok.bank.util.CreditCardNumberGenerator;
import com.bok.bank.util.exception.BankAccountException;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.bok.bank.repository.BankAccountRepository.Projections.BankAccountBasicInfo;
import static com.bok.bank.repository.CardRepository.Projection.CardInfo;
import static com.bok.bank.util.Constants.BIN_BOK;

@Component
public class CardHelper {

    @Autowired
    CardRepository cardRepository;
    @Autowired
    BankAccountRepository bankAccountRepository;
    @Autowired
    CreditCardNumberGenerator creditCardNumberGenerator;

    public List<CardInfoDTO> findAllCardsByAccount(Long accountId) {
        List<CardInfo> cardInfos = cardRepository.findAllCardInfoByAccountId(accountId);
        return cardInfos.stream()
                .map(cardInfo -> new CardInfoDTO(cardInfo.getCardId(), cardInfo.getName(), cardInfo.getCardStatus().name(), cardInfo.getType().name(), cardInfo.getLabel(), creditCardNumberGenerator.maskingPan(cardInfo.getMaskedPan())))
                .collect(Collectors.toList());
    }

    public CardInfoDTO findCardInfoById(Long accountId, Long cardId) {
        CardInfo cardInfo = cardRepository.findCardInfoByAccountIdAndCardId(accountId, cardId).orElseThrow(() -> new IllegalArgumentException("Card not found for cardId " + cardId + "and accountId " + accountId));
        return new CardInfoDTO(cardInfo.getCardId(), cardInfo.getName(), cardInfo.getCardStatus().name(), cardInfo.getType().name(), cardInfo.getLabel(), creditCardNumberGenerator.maskingPan(cardInfo.getMaskedPan()));
    }

    public String getPlainPan(Long accountId, Long cardId) {
        return cardRepository.findPlainPanByAccountIdAndCardId(accountId, cardId).orElseThrow(() -> new IllegalArgumentException("Card not found for cardId: " + cardId + " and accountId: " + accountId));
    }

    public int getCvv(Long accountId, Long cardId) {
        return cardRepository.findCvvByAccountIdAndCardId(accountId, cardId).orElseThrow(() -> new IllegalArgumentException("Card not found for cardId: " + cardId + " and accountId: " + accountId));
    }

    public CardInfoDTO createCard(Long accountId, CardDTO cardDTO) {
        BankAccountBasicInfo bankAccountBasicInfo = bankAccountRepository.findBankAccountBasicInfoByAccountId(accountId).orElseThrow(BankAccountException::new);
        Preconditions.checkArgument(bankAccountBasicInfo.getStatus().equals(BankAccount.Status.ACTIVE), "Fail creation card related to not active bank account, bankAccountId: " + bankAccountBasicInfo.getId());
        Card newCard = new Card(cardDTO.name, new Account(accountId), Card.CardStatus.TO_ACTIVATE, Card.Type.valueOf(cardDTO.type.trim()), Instant.now().plus(Period.ofYears(4).getDays(), ChronoUnit.DAYS),
                creditCardNumberGenerator.generateToken(), cardDTO.label, creditCardNumberGenerator.generate(BIN_BOK, 15), new BankAccount(bankAccountBasicInfo.getId()), creditCardNumberGenerator.generateCvv());
        newCard = cardRepository.save(newCard);
        return new CardInfoDTO(newCard.getId(), newCard.getName(), newCard.getCardStatus().name(), newCard.getType().name(), newCard.getLabel(), creditCardNumberGenerator.maskingPan(newCard.getMaskedPan()));
    }

    public void checkNewCardDTOData(CardDTO cardDTO) {
        Preconditions.checkArgument(cardDTO.name.trim().length() > 1, "Invalid card name : " + cardDTO.name.trim());
        Preconditions.checkArgument(cardDTO.type.trim().equals(Card.Type.CREDIT.name()) || cardDTO.type.trim().equals(Card.Type.DEBIT.name()) || cardDTO.type.trim().equals(Card.Type.VIRTUAL.name()) || cardDTO.type.trim().equals(Card.Type.ONE_USE.name()), "Invalid card type : " + cardDTO.type.trim());
    }
}
