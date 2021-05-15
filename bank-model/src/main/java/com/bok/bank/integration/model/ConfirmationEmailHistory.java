package com.bok.bank.integration.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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
import java.io.Serializable;
import java.time.Instant;

@Entity
public class ConfirmationEmailHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Account account;
    @Column
    private Long resourceId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    @Column(unique = true, nullable = false)
    private String confirmationToken;

    @CreationTimestamp
    private Instant creationTimestamp;
    @UpdateTimestamp
    private Instant updateTimestamp;

    public ConfirmationEmailHistory() {
    }

    public ConfirmationEmailHistory(Account account, Long resourceId, ResourceType resourceType, String confirmationToken) {
        this.account = account;
        this.resourceId = resourceId;
        this.resourceType = resourceType;
        this.confirmationToken = confirmationToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ConfirmationEmailHistory that = (ConfirmationEmailHistory) o;

        return new EqualsBuilder().append(id, that.id).append(account, that.account).append(resourceId, that.resourceId).append(resourceType, that.resourceType).append(confirmationToken, that.confirmationToken).append(creationTimestamp, that.creationTimestamp).append(updateTimestamp, that.updateTimestamp).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(account).append(resourceId).append(resourceType).append(confirmationToken).append(creationTimestamp).append(updateTimestamp).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("account", account)
                .append("resourceId", resourceId)
                .append("resourceType", resourceType)
                .append("confirmationToken", confirmationToken)
                .append("creationTimestamp", creationTimestamp)
                .append("updateTimestamp", updateTimestamp)
                .toString();
    }

    public enum ResourceType {
        BANK_ACCOUNT, CARD, TRANSACTION
    }

}
