package com.bok.bank.model;

import com.bok.bank.util.Money;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String publicId;
    @Column
    @Enumerated(EnumType.STRING)
    private Type type;
    @Column
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column
    private String fromMarket;
    @Column
    private String fromIban;
    @ManyToOne
    private BankAccount fromBankAccount;
    @ManyToOne
    private BankAccount toBankAccount;
    @ManyToOne
    private Card card;
    @ManyToOne
    private User transactionOwner;
    @Column
    @CreationTimestamp
    private Instant timestamp;
    @Embedded
    @AttributeOverride(name = "currency", column = @Column(name = "amount_currency"))
    @AttributeOverride(name = "value", column = @Column(name = "amount_value"))
    private Money amount;

    public Transaction() {
    }

    public Transaction(Type type, Status status, String fromMarket, BankAccount toBankAccount, Money amount, UUID publicId) {
        this.type = type;
        this.status = status;
        this.fromMarket = fromMarket;
        this.toBankAccount = toBankAccount;
        this.amount = amount;
        this.publicId = publicId.toString();
    }
    public Transaction(Type type, Status status, String fromMarket, BankAccount toBankAccount, Money amount, UUID publicId, Card card) {
        this.type = type;
        this.status = status;
        this.fromMarket = fromMarket;
        this.toBankAccount = toBankAccount;
        this.amount = amount;
        this.publicId = publicId.toString();
        this.card = card;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromMarket() {
        return fromMarket;
    }

    public void setFromMarket(String fromMarket) {
        this.fromMarket = fromMarket;
    }

    public String getFromIban() {
        return fromIban;
    }

    public void setFromIban(String fromIban) {
        this.fromIban = fromIban;
    }

    public BankAccount getFromBankAccount() {
        return fromBankAccount;
    }

    public void setFromBankAccount(BankAccount fromBankAccount) {
        this.fromBankAccount = fromBankAccount;
    }

    public BankAccount getToBankAccount() {
        return toBankAccount;
    }

    public void setToBankAccount(BankAccount toBankAccount) {
        this.toBankAccount = toBankAccount;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public User getTransactionOwner() {
        return transactionOwner;
    }

    public void setTransactionOwner(User transactionOwner) {
        this.transactionOwner = transactionOwner;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(UUID publicId) {
        this.publicId = publicId.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        return new EqualsBuilder().append(id, that.id).append(fromBankAccount, that.fromBankAccount).append(toBankAccount, that.toBankAccount).append(card, that.card).append(transactionOwner, that.transactionOwner).append(timestamp, that.timestamp).append(amount, that.amount).append(type, that.type).append(status, that.status).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(fromBankAccount).append(toBankAccount).append(card).append(transactionOwner).append(timestamp).append(amount).append(type).append(status).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("fromWallet", fromBankAccount)
                .append("toWallet", toBankAccount)
                .append("card", card)
                .append("transactionOwner", transactionOwner)
                .append("timestamp", timestamp)
                .append("amount", amount)
                .append("type", type)
                .append("status", status)
                .toString();
    }

    public enum Status {
        AUTHORISED,
        SETTLED,
        CANCELLED,
        DECLINED,
        UNKNOWN
    }

    public enum Type {
        PAYMENT,
        REFUND,
        LOAD,
        DEPOSIT,
        TRANSFER,
        CONVERSION,
        WIRE_TRANSFER,
        WITHDRAWAL,
        INTERNAL_ACTIVITY,
        BILLING,
        RECURRING_BILLING,
        NOT_RECOGNISED,
        INBOUND_FASTER_PAYMENT,
        INBOUND_FASTER_PAYMENT_REVERSAL,
        FEE,
        FEE_REVERSAL
    }
}
