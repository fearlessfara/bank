package com.bok.bank.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class WiredTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    private BankAccount senderAccount;

    @OneToOne
    private BankAccount beneficiaryAccount;

    @Column
    private String beneficiaryIBAN;

    @Column
    private BigDecimal amount;

    @Column
    private boolean instantTransfer;

    @Column
    private LocalDate executionDate;

    @Column
    @CreationTimestamp
    private Instant creationTimestamp;

    @Column
    @CreationTimestamp
    private Instant updateTimestamp;

    @Column
    private boolean processed;


}
