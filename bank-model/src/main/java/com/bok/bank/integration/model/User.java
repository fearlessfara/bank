package com.bok.bank.integration.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.Instant;

@Entity
@DiscriminatorValue("INDIVIDUAL_USER")
public class User extends Account implements Serializable {

    @Column
    private String middleName;

    @Column
    private String surname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(unique = true, updatable = false, length = 25)
    private String taxCode;

    @Column
    private String birthCity;

    @Column
    private String birthCountry;

    @Column
    private Instant birthDate;

    public User() {
        super();
    }

    public User(Long id, String name, String email, String mobile, String icc, Status status, String country, String county, String city, String postCode, String addLine, String civicNumber, String middlename, String surname, Gender gender, String taxCode, String birthCity, String birthCountry, Instant birthDate) {
        super(id, name, Type.INDIVIDUAL_USER, email, mobile, icc, status, country, county, city, postCode, addLine, civicNumber);
        this.middleName = middlename;
        this.surname = surname;
        this.gender = gender;
        this.taxCode = taxCode;
        this.birthCity = birthCity;
        this.birthCountry = birthCountry;
        this.birthDate = birthDate;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middlename) {
        this.middleName = middlename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
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

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return new EqualsBuilder().appendSuper(super.equals(o)).append(middleName, user.middleName).append(surname, user.surname).append(gender, user.gender).append(taxCode, user.taxCode).append(birthCity, user.birthCity).append(birthCountry, user.birthCountry).append(birthDate, user.birthDate).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(middleName).append(surname).append(gender).append(taxCode).append(birthCity).append(birthCountry).append(birthDate).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.toString())
                .append("middleName", middleName)
                .append("surname", surname)
                .append("gender", gender)
                .append("taxCode", taxCode)
                .append("birthCity", birthCity)
                .append("birthCountry", birthCountry)
                .append("birthDate", birthDate)
                .toString();
    }

    public enum Gender {
        M, F
    }
}

