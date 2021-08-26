package com.bok.bank.controller;

import com.bok.bank.helper.CardHelper;
import com.bok.bank.integration.dto.CardDTO;
import com.bok.bank.integration.dto.CardInfoDTO;
import com.bok.bank.integration.dto.PinDTO;
import com.bok.bank.integration.service.CardController;
import com.bok.bank.model.Card;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardControllerImpl implements CardController {

    @Autowired
    CardHelper cardHelper;

    @Override
    public List<CardInfoDTO> allCards(Long accountId) {
        return cardHelper.getAllCards(accountId);
    }

    @Override
    public CardInfoDTO findCard(Long accountId, Long cardId) {
        Preconditions.checkNotNull(cardId, "cardId must not be null");
        return cardHelper.getCardInfo(accountId, cardId);
    }

    @Override
    public String getPlainPan(Long accountId, Long cardId, PinDTO pinDTO) {
        Preconditions.checkNotNull(cardId, "cardId must not be null");
        cardHelper.checkPin(accountId, cardId, pinDTO.PIN);
        return cardHelper.getPlainPan(accountId, cardId);
    }

    @Override
    public String getCardToken(Long accountId, Long cardId) {
        Preconditions.checkNotNull(cardId, "cardId must not be null");
        return cardHelper.getCardToken(accountId, cardId);
    }

    @Override
    public Integer getCvv(Long accountId, Long cardId, PinDTO pinDTO) {
        Preconditions.checkNotNull(cardId, "cardId must not be null");
        cardHelper.checkPin(accountId, cardId, pinDTO.PIN);
        return cardHelper.getCvv(accountId, cardId);
    }

    @Override
    public String getPIN(Long accountId, Long cardId) {
        Preconditions.checkNotNull(cardId, "cardId must not be null");
        return cardHelper.getPIN(accountId, cardId);
    }

    @Override
    public String changeCardStatus(Long accountId, Long cardId, String status, PinDTO pinDTO) {
        Preconditions.checkNotNull(cardId, "cardId must not be null");
        Preconditions.checkNotNull(status, "status must not be null");
        Preconditions.checkNotNull(pinDTO, "pinDTO must not be null");
        cardHelper.checkPin(accountId, cardId, pinDTO.PIN);
        return cardHelper.changeCardStatus(cardId, Card.CardStatus.valueOf(status));
    }

    @Override
    public String createCard(Long accountId, CardDTO cardDTO) {
        cardHelper.checkNewCardDTOData(cardDTO);
        return cardHelper.createCard(accountId, cardDTO);
    }

    @Override
    public String activation(Long accountId, Long cardId, PinDTO pinDTO) {
        Preconditions.checkArgument(StringUtils.isNotBlank(pinDTO.PIN.trim()), "configurationToken is blank");
        cardHelper.checkPin(accountId, cardId, pinDTO.PIN);
        return cardHelper.changeCardStatus(cardId, Card.CardStatus.ACTIVE);
    }
    @Override
    public String delete(Long accountId, Long cardId, PinDTO pinDTO) {
        Preconditions.checkArgument(StringUtils.isNotBlank(pinDTO.PIN.trim()), "configurationToken is blank");
        cardHelper.checkPin(accountId, cardId, pinDTO.PIN);
        return cardHelper.changeCardStatus(cardId, Card.CardStatus.DESTROYED);
    }
}
