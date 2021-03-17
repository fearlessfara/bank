package com.bok.bank.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Type type;

    @Column(nullable = false, unique = true, updatable = false)
    private String username;

    @Column
    private String vatNumber;

    @Column(nullable = false, unique = true, updatable = false)
    private String taxCode;

    @Column
    private String middlename;

    @Column
    private String surname;

    @Column
    private String gender;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String mobile;

    @Column(nullable = true)
    private String icc;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private String country;

    @Column
    private String birthCity;

    @Column
    private String birthCountry;

    @Column
    private LocalDate birthDate;

    @Column
    private String nationality;

    @Column
    private String addLine1;

    @Column
    private String addLine2;

    @Column
    private String addLine3;

    @Column
    private String addCity;

    @Column
    private String addPostCode;

    @Column
    private String addCounty;

    @Column
    private String addCountry;

    public User() {
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

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getBirthCity() {
        return birthCity;
    }

    public void setBirthCity(String birthCity) {
        this.birthCity = birthCity;
    }

    public String getBirthCountry() {
        return birthCountry;
    }

    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getAddLine1() {
        return addLine1;
    }

    public void setAddLine1(String addLine1) {
        this.addLine1 = addLine1;
    }

    public String getAddLine2() {
        return addLine2;
    }

    public void setAddLine2(String addLine2) {
        this.addLine2 = addLine2;
    }

    public String getAddLine3() {
        return addLine3;
    }

    public void setAddLine3(String addLine3) {
        this.addLine3 = addLine3;
    }

    public String getAddCity() {
        return addCity;
    }

    public void setAddCity(String addCity) {
        this.addCity = addCity;
    }

    public String getAddPostCode() {
        return addPostCode;
    }

    public void setAddPostCode(String addPostCode) {
        this.addPostCode = addPostCode;
    }

    public String getAddCounty() {
        return addCounty;
    }

    public void setAddCounty(String addCounty) {
        this.addCounty = addCounty;
    }

    public String getAddCountry() {
        return addCountry;
    }

    public void setAddCountry(String addCountry) {
        this.addCountry = addCountry;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("type", type)
                .append("username", username)
                .append("vatNumber", vatNumber)
                .append("taxCode", taxCode)
                .append("middlename", middlename)
                .append("surname", surname)
                .append("gender", gender)
                .append("email", email)
                .append("mobile", mobile)
                .append("icc", icc)
                .append("status", status)
                .append("country", country)
                .append("birthCity", birthCity)
                .append("birthCountry", birthCountry)
                .append("birthDate", birthDate)
                .append("nationality", nationality)
                .append("addLine1", addLine1)
                .append("addLine2", addLine2)
                .append("addLine3", addLine3)
                .append("addCity", addCity)
                .append("addPostCode", addPostCode)
                .append("addCounty", addCounty)
                .append("addCountry", addCountry)
                .toString();
    }

    public enum Status {
        ACTIVE, BLOCKED, SUSPENDED, PENDING, ARCHIVED, DELETED
    }

    public enum Type {
        COMPANY, INDIVIDUAL_USER
    }

}

