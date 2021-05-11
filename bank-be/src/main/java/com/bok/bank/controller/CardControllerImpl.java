package com.bok.bank.controller;

import com.bok.bank.dto.CardInfoDTO;
import com.bok.bank.dto.CardDTO;
import com.bok.bank.helper.CardHelper;
import com.bok.bank.service.CardController;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardControllerImpl implements CardController {

    @Autowired
    CardHelper cardHelper;

    @Override
    public List<CardInfoDTO> allCards(Long accountId) {
        return cardHelper.findAllCardsByAccount(accountId);
    }

    @Override
    public CardInfoDTO findCard(Long accountId, Long cardId) {
        Preconditions.checkNotNull(cardId, "cardId must not be null");
        return cardHelper.findCardInfoById(accountId, cardId);
    }

    @Override
    public String getPlainPan(Long accountId, Long cardId) {
        Preconditions.checkNotNull(cardId, "cardId must not be null");
        return cardHelper.getPlainPan(accountId, cardId);
    }

    @Override
    public Integer getCvv(Long accountId, Long cardId) {
        Preconditions.checkNotNull(cardId, "cardId must not be null");
        return cardHelper.getCvv(accountId, cardId);
    }

    @Override
    public CardInfoDTO newCard(Long accountId, CardDTO cardDTO) {
        cardHelper.checkNewCardDTOData(cardDTO);
        return cardHelper.createCard(accountId, cardDTO);
    }
}
