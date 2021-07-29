package com.bok.bank.model;


import com.bok.bank.util.Money;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@Entity
public class BankAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long accountId;

    @Column(nullable = false, unique = true)
    private String IBAN;

    @Column(nullable = false)
    private String name;

    @Column
    private String label;

    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Embedded
    @AttributeOverride(name = "currency", column = @Column(name = "blocked_amount_currency"))
    @AttributeOverride(name = "value", column = @Column(name = "blocked_amount_value"))
    private Money blockedAmount;

    @Embedded
    @AttributeOverride(name = "currency", column = @Column(name = "available_amount_currency"))
    @AttributeOverride(name = "value", column = @Column(name = "available_amount_value"))
    private Money availableAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @CreationTimestamp
    private Instant creationTimestamp;

    @UpdateTimestamp
    private Instant updateTimestamp;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.MERGE)
    private List<Card> cards = new ArrayList<>();

    public BankAccount() {
    }

    public BankAccount(Long id) {
        this.id = id;
    }

    public BankAccount(Long accountId, String IBAN, String name, String label, Currency currency, Money blockedAmount, Money availableAmount, Status status) {
        this.accountId = accountId;
        this.IBAN = IBAN;
        this.name = name;
        this.label = label;
        this.currency = currency;
        this.blockedAmount = blockedAmount;
        this.availableAmount = availableAmount;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Money getBlockedAmount() {
        return blockedAmount;
    }

    public void setBlockedAmount(Money blockedAmount) {
        this.blockedAmount = blockedAmount;
    }

    public Money getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(Money availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("accountId", accountId)
                .append("IBAN", IBAN)
                .append("name", name)
                .append("label", label)
                .append("currency", currency)
                .append("blockedAmount", blockedAmount)
                .append("availableAmount", availableAmount)
                .append("status", status)
                .append("creationTimestamp", creationTimestamp)
                .append("updateTimestamp", updateTimestamp)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BankAccount that = (BankAccount) o;

        return new EqualsBuilder().append(id, that.id).append(accountId, that.accountId).append(IBAN, that.IBAN).append(name, that.name).append(label, that.label).append(currency, that.currency).append(blockedAmount, that.blockedAmount).append(availableAmount, that.availableAmount).append(status, that.status).append(creationTimestamp, that.creationTimestamp).append(updateTimestamp, that.updateTimestamp).append(cards, that.cards).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(accountId).append(IBAN).append(name).append(label).append(currency).append(blockedAmount).append(availableAmount).append(status).append(creationTimestamp).append(updateTimestamp).append(cards).toHashCode();
    }

    public enum Status {
        ACTIVE, BLOCKED, SUSPENDED, PENDING, ARCHIVED, DELETED
    }
}
