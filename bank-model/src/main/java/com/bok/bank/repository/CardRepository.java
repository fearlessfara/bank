package com.bok.bank.repository;

import com.bok.bank.model.Account;
import com.bok.bank.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    int deleteByAccount(Account account);

    @Query("SELECT c.id as cardId, c.name as name, c.cardStatus as cardStatus, c.type as type, c.label as label, c.maskedPan as maskedPan, c.cvv as cvv " +
            "FROM Card c WHERE c.account.id = :accountId")
    List<Projection.CardInfo> findAllCardInfoByAccountId(Long accountId);

    @Query("SELECT c.id as cardId, c.name as name, c.cardStatus as cardStatus, c.type as type, c.label as label, c.maskedPan as maskedPan, c.cvv as cvv " +
            "FROM Card c WHERE c.account.id = :accountId AND c.id = :cardId")
    Optional<Projection.CardInfo> findCardInfoByAccountIdAndCardId(Long accountId, Long cardId);

    @Query("SELECT c.maskedPan " +
            "FROM Card c WHERE c.account.id = :accountId AND c.id = :cardId")
    Optional<String> findPlainPanByAccountIdAndCardId(Long accountId, Long cardId);

    @Query("SELECT c.token " +
            "FROM Card c WHERE c.account.id = :accountId AND c.id = :cardId")
    Optional<String> findCardTokenByAccountIdAndCardId(Long accountId, Long cardId);

    @Query("SELECT c.cvv " +
            "FROM Card c WHERE c.account.id = :accountId AND c.id = :cardId")
    Optional<Integer> findCvvByAccountIdAndCardId(Long accountId, Long cardId);

    @Query("UPDATE Card set cardStatus = :status where id= :cardId")
    int changeCardStatus(Long cardId, Card.CardStatus status);

    Optional<Card> findByToken(String token);

    class Projection {
        public interface CardInfo {
            Long getCardId();

            String getName();

            Card.CardStatus getCardStatus();

            Card.Type getType();

            String getLabel();

            String getMaskedPan();

            Integer getCvv();
        }
    }

}
