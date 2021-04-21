package com.bok.bank.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type",
        discriminatorType = DiscriminatorType.STRING)
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private Type type;

    @Column(nullable = false, unique = true, updatable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String mobile;

    @Column
    private String icc;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private String country = "";

    @Column
    private String county = "";

    @Column(nullable = false)
    private String city = "";

    @Column(nullable = false)
    private String postCode = "";

    @Column
    private String addLine;

    @Column
    private String civicNumber;

    @CreationTimestamp
    private Instant creationTimestamp;

    @UpdateTimestamp
    private Instant updateTimestamp;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.MERGE)
    private List<Card> cards = new ArrayList<>();

    @OneToOne
    private BankAccount bankAccount;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.MERGE)
    private List<Transaction> transactions = new ArrayList<>();

    public Account() {
    }

    public Account(String name, Type type, String username, String email, String mobile, String icc, Status status, String country, String county, String city, String postCode, String addLine, String civicNumber) {
        this.name = name;
        this.type = type;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.icc = icc;
        this.status = status;
        this.country = country;
        this.county = county;
        this.city = city;
        this.postCode = postCode;
        this.addLine = addLine;
        this.civicNumber = civicNumber;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIcc() {
        return icc;
    }

    public void setIcc(String icc) {
        this.icc = icc;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getAddLine() {
        return addLine;
    }

    public void setAddLine(String addLine) {
        this.addLine = addLine;
    }

    public String getCivicNumber() {
        return civicNumber;
    }

    public void setCivicNumber(String civicNumber) {
        this.civicNumber = civicNumber;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("type", type)
                .append("username", username)
                .append("email", email)
                .append("mobile", mobile)
                .append("icc", icc)
                .append("status", status)
                .append("country", country)
                .append("county", county)
                .append("city", city)
                .append("postCode", postCode)
                .append("addLine", addLine)
                .append("civicNumber", civicNumber)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return new EqualsBuilder().append(id, account.id).append(name, account.name).append(type, account.type).append(username, account.username).append(email, account.email).append(mobile, account.mobile).append(icc, account.icc).append(status, account.status).append(country, account.country).append(county, account.county).append(city, account.city).append(postCode, account.postCode).append(addLine, account.addLine).append(civicNumber, account.civicNumber).append(cards, account.cards).append(bankAccount, account.bankAccount).append(transactions, account.transactions).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).append(type).append(username).append(email).append(mobile).append(icc).append(status).append(country).append(county).append(city).append(postCode).append(addLine).append(civicNumber).append(cards).append(bankAccount).append(transactions).toHashCode();
    }

    public enum Status {
        ACTIVE, BLOCKED, SUSPENDED, PENDING, ARCHIVED, DELETED
    }

    public enum Type {
        COMPANY, INDIVIDUAL_USER
    }
}
