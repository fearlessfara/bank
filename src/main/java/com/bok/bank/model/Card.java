package com.bok.bank.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.time.Instant;
import java.util.Currency;
import java.util.UUID;

@Entity
public class Card implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Account.Type holderType;

    @Enumerated(EnumType.STRING)
    private CardStatus CardStatus;

    @Column(nullable = false)
    private Instant expirationDate;

    @Column(unique = true, nullable = false)
    private String token;

    @Column(name = "currency", nullable = false, updatable = false)
    private Currency currency;

    @Column
    private String label;

    @Column(nullable = false)
    private String maskedpan;

    //Delivery address
    @Column
    private String delvAddLine1;

    @Column
    private String delvAddLine2;

    @Column
    private String delvAddLine3;

    @Column
    private String delvCity;

    @Column
    private String delvPostCode;

    @Column
    private String delvCounty;

    @Column
    private String delvCountry;

    @Column(nullable = false, updatable = false)
    private String publicId = UUID.randomUUID().toString();

    @CreationTimestamp
    private Instant creationTimestamp;

    @UpdateTimestamp
    private Instant updateTimestamp;

    @ManyToOne
    private Wallet wallet;

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

    public Account.Type getHolderType() {
        return holderType;
    }

    public void setHolderType(Account.Type holderType) {
        this.holderType = holderType;
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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
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

    public String getDelvAddLine1() {
        return delvAddLine1;
    }

    public void setDelvAddLine1(String delvAddLine1) {
        this.delvAddLine1 = delvAddLine1;
    }

    public String getDelvAddLine2() {
        return delvAddLine2;
    }

    public void setDelvAddLine2(String delvAddLine2) {
        this.delvAddLine2 = delvAddLine2;
    }

    public String getDelvAddLine3() {
        return delvAddLine3;
    }

    public void setDelvAddLine3(String delvAddLine3) {
        this.delvAddLine3 = delvAddLine3;
    }

    public String getDelvCity() {
        return delvCity;
    }

    public void setDelvCity(String delvCity) {
        this.delvCity = delvCity;
    }

    public String getDelvPostCode() {
        return delvPostCode;
    }

    public void setDelvPostCode(String delvPostCode) {
        this.delvPostCode = delvPostCode;
    }

    public String getDelvCounty() {
        return delvCounty;
    }

    public void setDelvCounty(String delvCounty) {
        this.delvCounty = delvCounty;
    }

    public String getDelvCountry() {
        return delvCountry;
    }

    public void setDelvCountry(String delvCountry) {
        this.delvCountry = delvCountry;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
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
                .append("holderType", holderType)
                .append("CardStatus", CardStatus)
                .append("expirationDate", expirationDate)
                .append("token", token)
                .append("currency", currency)
                .append("label", label)
                .append("maskedpan", maskedpan)
                .append("delvAddLine1", delvAddLine1)
                .append("delvAddLine2", delvAddLine2)
                .append("delvAddLine3", delvAddLine3)
                .append("delvCity", delvCity)
                .append("delvPostCode", delvPostCode)
                .append("delvCounty", delvCounty)
                .append("delvCountry", delvCountry)
                .append("publicId", publicId)
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
}
