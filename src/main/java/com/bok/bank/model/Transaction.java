package com.bok.bank.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.Instant;

@Entity
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Wallet fromWallet;

    @ManyToOne
    private Wallet toWallet;

    @ManyToOne
    private Card card;

    @ManyToOne
    private User transactionOwner;

    @Column
    @CreationTimestamp
    private Instant timestamp;

    @Column
    private Money amount;

    @Column
    @Enumerated(EnumType.STRING)
    public Type type;

    @Column
    @Enumerated(EnumType.STRING)
    public Status status;

    @ManyToOne
    private User user;

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "activityId")
//    private List<TransactionHistory> timeline = new ArrayList<>();


    public Transaction() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Wallet getFromWallet() {
        return fromWallet;
    }

    public void setFromWallet(Wallet fromWallet) {
        this.fromWallet = fromWallet;
    }

    public Wallet getToWallet() {
        return toWallet;
    }

    public void setToWallet(Wallet toWallet) {
        this.toWallet = toWallet;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("fromWallet", fromWallet)
                .append("toWallet", toWallet)
                .append("card", card)
                .append("transactionOwner", transactionOwner)
                .append("timestamp", timestamp)
                .append("amount", amount)
                .append("type", type)
                .append("status", status)
                .append("user", user)
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
        LOAD_REVERSAL,
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
