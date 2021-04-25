package com.bok.bank.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.CascadeType;
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
import java.util.Currency;
import java.util.List;

@Entity
public class Card implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    private Account account;

    @Column
    @Enumerated(EnumType.STRING)
    private CardStatus cardStatus;

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
    private String maskedPan;

    @Column(nullable = false)
    private int cvv;

    @CreationTimestamp
    private Instant creationTimestamp;

    @UpdateTimestamp
    private Instant updateTimestamp;

    @ManyToOne
    private BankAccount bankAccount;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.MERGE)
    private List<Transaction> transactions = new ArrayList<>();


    public Card() {
    }

    public Card(String name, Account account, CardStatus cardStatus, Type type, Instant expirationDate, String token, String label, String maskedPan, BankAccount bankAccount, int cvv) {
        this.name = name;
        this.account = account;
        this.cardStatus = cardStatus;
        this.type = type;
        this.expirationDate = expirationDate;
        this.token = token;
        this.label = label;
        this.maskedPan = maskedPan;
        this.bankAccount = bankAccount;
        this.cvv = cvv;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public CardStatus getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(CardStatus cardStatus) {
        this.cardStatus = cardStatus;
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

    public String getMaskedPan() {
        return maskedPan;
    }

    public void setMaskedPan(String maskedPan) {
        this.maskedPan = maskedPan;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public BankAccount getWallet() {
        return bankAccount;
    }

    public void setWallet(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        return new EqualsBuilder().append(id, card.id).append(name, card.name).append(account, card.account).append(cardStatus, card.cardStatus).append(type, card.type).append(expirationDate, card.expirationDate).append(token, card.token).append(label, card.label).append(maskedPan, card.maskedPan).append(creationTimestamp, card.creationTimestamp).append(updateTimestamp, card.updateTimestamp).append(bankAccount, card.bankAccount).append(transactions, card.transactions).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).append(account).append(cardStatus).append(type).append(expirationDate).append(token).append(label).append(maskedPan).append(creationTimestamp).append(updateTimestamp).append(bankAccount).append(transactions).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("user", account)
                .append("CardStatus", cardStatus)
                .append("expirationDate", expirationDate)
                .append("token", token)
                .append("label", label)
                .append("maskedpan", maskedPan)
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
