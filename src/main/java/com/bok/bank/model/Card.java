package com.bok.bank.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Card implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    private User user;

    @Column
    @Enumerated(EnumType.STRING)
    private CardStatus CardStatus;

    @Column
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false)
    private Instant expirationDate;

    @Column(unique = true, nullable = false)
    private String token;


    @Column
    private String label;

    @Column(nullable = false)
    private String maskedpan;

    @CreationTimestamp
    private Instant creationTimestamp;

    @UpdateTimestamp
    private Instant updateTimestamp;

    @ManyToOne
    private Wallet wallet;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();


    public Card() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Card.CardStatus getCardStatus() {
        return CardStatus;
    }

    public void setCardStatus(Card.CardStatus cardStatus) {
        CardStatus = cardStatus;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMaskedpan() {
        return maskedpan;
    }

    public void setMaskedpan(String maskedpan) {
        this.maskedpan = maskedpan;
    }

    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Instant creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Instant getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Instant updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("user", user)
                .append("CardStatus", CardStatus)
                .append("expirationDate", expirationDate)
                .append("token", token)
                .append("label", label)
                .append("maskedpan", maskedpan)
                .append("creationTimestamp", creationTimestamp)
                .append("updateTimestamp", updateTimestamp)
                .toString();
    }

    public enum CardStatus {
        TO_ACTIVATE,
        DEACTIVATED,
        ACTIVE,
        LOST,
        STOLEN,
        BROKEN,
        DESTROYED
    }

    public enum Type {
        CREDIT, DEBIT, VIRTUAL, ONE_USE
    }
}
