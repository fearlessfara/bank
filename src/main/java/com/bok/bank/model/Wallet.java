package com.bok.bank.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.Instant;
import java.util.Currency;

@Entity
public class Wallet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column
    private String label;

    @Column(name = "currency", nullable = false, updatable = false)
    private Currency currency;

    @Embedded
    private Money availableAmount;

    @Embedded
    private Money blockedAmount;

    @Column(nullable = false, updatable = false)
    private String publicId;

    @Column(name = "note")
    private String note;

    @CreationTimestamp
    private Instant creationTimestamp;

    @UpdateTimestamp
    private Instant updateTimestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Money getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(Money availableAmount) {
        this.availableAmount = availableAmount;
    }

    public Money getBlockedAmount() {
        return blockedAmount;
    }

    public void setBlockedAmount(Money blockedAmount) {
        this.blockedAmount = blockedAmount;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
                .append("user", user)
                .append("name", name)
                .append("label", label)
                .append("currency", currency)
                .append("availableAmount", availableAmount)
                .append("blockedAmount", blockedAmount)
                .append("publicId", publicId)
                .append("note", note)
                .append("creationTimestamp", creationTimestamp)
                .append("updateTimestamp", updateTimestamp)
                .toString();
    }
}
