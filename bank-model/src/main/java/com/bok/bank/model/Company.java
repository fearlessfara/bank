package com.bok.bank.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("COMPANY")
public class Company extends Account {

    @Column(unique = true, updatable = false, length = 15)
    private String vatNumber;

    public Company() {
    }

    public Company(String name, String username, String email, String mobile, String icc, Status status, String country, String county, String city, String postCode, String addLine, String civicNumber, String vatNumber) {
        super(name, Type.COMPANY, username, email, mobile, icc, status, country, county, city, postCode, addLine, civicNumber);
        this.vatNumber = vatNumber;
    }

    public Company(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;

        return new EqualsBuilder().appendSuper(super.equals(o)).append(vatNumber, company.vatNumber).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(vatNumber).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("vatNumber", vatNumber)
                .toString();
    }
}
