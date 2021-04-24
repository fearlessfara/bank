package com.bok.bank.controller;

import com.bok.bank.dto.CardInfoDTO;
import com.bok.bank.dto.NewCardDTO;
import com.bok.bank.helper.CardHelper;
import com.bok.bank.service.CardController;
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
        return cardHelper.findCardInfoById(accountId, cardId);
    }

    @Override
    public CardInfoDTO newCard(Long accountId, NewCardDTO newCardDTO) {
        cardHelper.checkNewCardDTOData(newCardDTO);
        return cardHelper.createCard(accountId, newCardDTO);
    }
}
