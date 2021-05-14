package com.bok.bank.controller;

import com.bok.bank.model.Account;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.Card;
import com.bok.bank.model.Company;
import com.bok.bank.model.User;
import com.bok.bank.repository.AccountRepository;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.repository.CardRepository;
import com.bok.bank.service.MainController;
import com.bok.bank.util.CreditCardNumberGenerator;
import com.bok.bank.util.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import static com.bok.bank.util.Constants.BIN_BOK;

/**
 * This is a service to delete
 */
@Service
public class MainControllerImpl implements MainController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CreditCardNumberGenerator creditCardNumberGenerator;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    CardRepository cardRepository;

    @Override
    public String populateDB() {
        List<Account> accounts = Arrays.asList(
                new User(4997L, "Domenico", "mico.fasano@gmail.com", "3926772950", "+39", Account.Status.ACTIVE, "Italy", "Apulia", "Locorotondo", "70010",
                        "via le mani dal naso", "12/c", null, "Fasano", User.Gender.M, "FSNDNC99C13D508Y", "Fasano", "Italy", LocalDate.of(1999, 3, 13).atStartOfDay().toInstant(ZoneOffset.UTC)),
                new User(4998L, "Chris", "chris.fara@gmail.com", "3911172950", "+39", Account.Status.ACTIVE, "Italy", "CalaBBria", "GIOIA TAURA", "00000",
                        "via le mani dal ferro", "69/c", "Gennaro", "Faraone", User.Gender.M, "CSTFRN99B44D508Y", "ViBBo Violenza", "Italy", LocalDate.of(1999, 8, 21).atStartOfDay().toInstant(ZoneOffset.UTC)),
                new User(4999L, "Paolo", "paolo.pio@gmail.com", "3911172150", "+39", Account.Status.ACTIVE, "Italy", "Apulia", "San Marco In Lamis", "00001",
                        "via le mani dai taralli", "00/c", "Pio", "Bevilacqua", User.Gender.M, "PLPBLQ99O99D508Y", "San Giovanni Rotondo", "Italy", LocalDate.of(1999, 10, 2).atStartOfDay().toInstant(ZoneOffset.UTC)),
                new Company(5000L, "Soldo", "soldo.soldo@soldo.com", "1234567890", "+39", Account.Status.ACTIVE, "Italy", "Lazio", "Rome", "00159",
                        "via le mani dal soldo", "2/c", "123422342"),
                new Company(5001L, "Softlab", "soft.lab@softlab.com", "1234562290", "+39", Account.Status.ACTIVE, "Italy", "Lazio", "Rome", "00159",
                        "via le mani dal bell stu sit", "13/c", "123499342")
        );
        List<BankAccount> bankAccounts = Arrays.asList(
                new BankAccount(accounts.stream().filter(account -> account.getName().equals("Domenico")).findFirst().get(), "1234543212345432123454321234", "firstBankAccount", "universitary", Currency.getInstance("EUR"), new Money(BigDecimal.ZERO), new Money(BigDecimal.valueOf(100)), BankAccount.Status.ACTIVE),
                new BankAccount(accounts.stream().filter(account -> account.getName().equals("Chris")).findFirst().get(), "1234543212311112123454321234", "AeroBankAccount", "boh", Currency.getInstance("EUR"), new Money(BigDecimal.ZERO), new Money(BigDecimal.valueOf(50)), BankAccount.Status.ACTIVE)
        );
        List<Card> cards = Arrays.asList(
                new Card("firstCard", accounts.stream().filter(account -> account.getName().equals("Domenico")).findFirst().get(), Card.CardStatus.ACTIVE, Card.Type.DEBIT, Instant.now().plus(Period.ofYears(4).getDays(), ChronoUnit.DAYS), creditCardNumberGenerator.generateToken(), "prova", creditCardNumberGenerator.generate(BIN_BOK, 15), bankAccounts.stream().filter(bankAccount -> bankAccount.getAccount().getName().equals("Domenico")).findFirst().get(), 123),
                new Card("firstVirtualCard", accounts.stream().filter(account -> account.getName().equals("Chris")).findFirst().get(), Card.CardStatus.ACTIVE, Card.Type.VIRTUAL, Instant.now().plus(Period.ofYears(4).getDays(), ChronoUnit.DAYS), creditCardNumberGenerator.generateToken(), "provaV", creditCardNumberGenerator.generate(BIN_BOK, 15), bankAccounts.stream().filter(bankAccount -> bankAccount.getAccount().getName().equals("Chris")).findFirst().get(), 371)
        );
        accountRepository.saveAll(accounts);
        bankAccountRepository.saveAll(bankAccounts);
        cardRepository.saveAll(cards);
        return "populated";
    }


    @Override
    public String cleanDB() {
        cardRepository.deleteAll();
        bankAccountRepository.deleteAll();
        accountRepository.deleteAll();
        return "cleaned";
    }
}
