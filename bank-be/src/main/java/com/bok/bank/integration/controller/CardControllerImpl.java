package com.bok.bank.integration.controller;

import com.bok.bank.integration.dto.CardInfoDTO;
import com.bok.bank.integration.dto.CardDTO;
import com.bok.bank.integration.helper.CardHelper;
import com.bok.bank.integration.service.CardController;
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
    public String createCard(Long accountId, CardDTO cardDTO) {
        cardHelper.checkNewCardDTOData(cardDTO);
        return cardHelper.createCard(accountId, cardDTO);
    }

    @Override
    public CardInfoDTO verify(Long accountId, String confirmationToken) {
        Preconditions.checkArgument(StringUtils.isNotBlank(confirmationToken.trim()), "configurationToken is blank");
        return cardHelper.verifyCard(accountId, confirmationToken);

    }
}
