package com.bok.bank.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("INDIVIDUAL_USER")
public class User extends Account implements Serializable {

    @Column
    private String middlename;

    @Column
    private String surname;

    @Column
    private Gender gender;

    @Column(unique = true, updatable = false, length = 25)
    private String taxCode;

    @Column
    private String birthCity;

    @Column
    private String birthCountry;

    @Column
    private LocalDate birthDate;

    public User() {
        super();
    }

    public User(String name, String username, String email, String mobile, String icc, Status status, String country, String county, String city, String postCode, String addLine, String civicNumber, String middlename, String surname, Gender gender, String taxCode, String birthCity, String birthCountry, LocalDate birthDate) {
        super(name, Type.INDIVIDUAL_USER, username, email, mobile, icc, status, country, county, city, postCode, addLine, civicNumber);
        this.middlename = middlename;
        this.surname = surname;
        this.gender = gender;
        this.taxCode = taxCode;
        this.birthCity = birthCity;
        this.birthCountry = birthCountry;
        this.birthDate = birthDate;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return new EqualsBuilder().appendSuper(super.equals(o)).append(middlename, user.middlename).append(surname, user.surname).append(gender, user.gender).append(taxCode, user.taxCode).append(birthCity, user.birthCity).append(birthCountry, user.birthCountry).append(birthDate, user.birthDate).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(middlename).append(surname).append(gender).append(taxCode).append(birthCity).append(birthCountry).append(birthDate).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("middlename", middlename)
                .append("surname", surname)
                .append("gender", gender)
                .append("taxCode", taxCode)
                .append("birthCity", birthCity)
                .append("birthCountry", birthCountry)
                .append("birthDate", birthDate)
                .toString();
    }

    public enum Gender {
        M,F
    }
}

