package com.bok.bank.helper;

import com.bok.bank.dto.CardInfoDTO;
import com.bok.bank.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CardHelper {

    @Autowired
    CardRepository cardRepository;

    public List<CardInfoDTO> findAllCardsByAccount(Long accountId){
        List<CardRepository.Projection.CardInfo> cardInfos = cardRepository.findAllCardInfoByAccountId(accountId);
        return cardInfos.stream()
                .map( cardInfo -> new CardInfoDTO(cardInfo.getName(), cardInfo.getCardStatus().name(), cardInfo.getType().name(), cardInfo.getCurrency(), cardInfo.getLabel(), cardInfo.getMaskedPan(), cardInfo.getCvv()))
                .collect(Collectors.toList());
    }

    public CardInfoDTO findCardInfoById(Long accountId, Long cardId){
        CardRepository.Projection.CardInfo cardInfo = cardRepository.findCardInfoByAccountIdAndCardId(accountId, cardId).orElseThrow(() -> new IllegalArgumentException("Card not found for cardId " + cardId));
        return  new CardInfoDTO(cardInfo.getName(), cardInfo.getCardStatus().name(), cardInfo.getType().name(), cardInfo.getCurrency(), cardInfo.getLabel(), cardInfo.getMaskedPan(), cardInfo.getCvv());
    }
}
