package com.bok.bank.repository;

import com.bok.bank.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("SELECT c.name as name, c.cardStatus as cardStatus, c.type as type, c.currency as currency, c.label as label, c.maskedPan as maskedPan, c.cvv as cvv " +
            "FROM Card c WHERE c.account.id = :accountId")
    List<Projection.CardInfo> findAllCardInfoByAccountId(Long accountId);

    @Query("SELECT c.name as name, c.cardStatus as cardStatus, c.type as type, c.currency as currency, c.label as label, c.maskedPan as maskedPan, c.cvv as cvv " +
            "FROM Card c WHERE c.account.id = :accountId AND c.id = :cardId")
    Optional<Projection.CardInfo> findCardInfoByAccountIdAndCardId(Long accountId, Long cardId);

    class Projection {
        public interface CardInfo{
            String getName();
            Card.CardStatus getCardStatus();
            Card.Type getType();
            Currency getCurrency();
            String getLabel();
            String getMaskedPan();
            Integer getCvv();
        }
    }

}
