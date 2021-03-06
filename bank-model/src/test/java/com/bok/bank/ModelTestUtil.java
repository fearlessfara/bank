package com.bok.bank;

import com.bok.bank.model.Account;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.Company;
import com.bok.bank.model.ExchangeCurrencyValue;
import com.bok.bank.model.ExchangeCurrencyValueHistory;
import com.bok.bank.model.User;
import com.bok.bank.repository.AccountRepository;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.repository.ExchangeCurrencyValueHistoryRepository;
import com.bok.bank.repository.ExchangeCurrencyValueRepository;
import com.bok.bank.util.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModelTestUtil {

    @Autowired
    ExchangeCurrencyValueHistoryRepository exchangeCurrencyValueHistoryRepository;

    @Autowired
    ExchangeCurrencyValueRepository exchangeCurrencyValueRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BankAccountRepository bankAccountRepository;


    public synchronized void populateDB() {
        List<Account> accounts = Arrays.asList(
                new User("Domenico", "domfas", "mico.fasano@gmail.com", "3926772950", "+39", Account.Status.ACTIVE, "Italy", "Apulia", "Locorotondo", "70010",
                        "via le mani dal naso", "12/c", null, "Fasano", User.Gender.M, "FSNDNC99C13D508Y", "Fasano", "Italy", LocalDate.of(1999, 3, 13)),
                new User("Chris", "chrisfara", "chris.fara@gmail.com", "3911172950", "+39", Account.Status.ACTIVE, "Italy", "CalaBBria", "GIOIA TAURA", "00000",
                        "via le mani dal ferro", "69/c", "Gennaro", "Faraone", User.Gender.M, "CSTFRN99B44D508Y", "ViBBo Violenza", "Italy", LocalDate.of(1999, 8, 21)),
                new User("Paolo", "paolopi", "paolo.pio@gmail.com", "3911172150", "+39", Account.Status.ACTIVE, "Italy", "Apulia", "San Marco In Lamis", "00001",
                        "via le mani dai taralli", "00/c", "Pio", "Bevilacqua", User.Gender.M, "PLPBLQ99O99D508Y", "San Giovanni Rotondo", "Italy", LocalDate.of(1999, 10, 2)),
                new Company("Soldo", "sldin", "soldo.soldo@soldo.com", "1234567890", "+39", Account.Status.ACTIVE, "Italy", "Lazio", "Rome", "00159",
                        "via le mani dal soldo", "2/c", "123422342"),
                new Company("Softlab", "sftlb", "soft.lab@softlab.com", "1234562290", "+39", Account.Status.ACTIVE, "Italy", "Lazio", "Rome", "00159",
                        "via le mani dal bell stu sit", "13/c", "123499342")
        );
        accountRepository.saveAll(accounts);

        List<BankAccount> bankAccounts = Arrays.asList(
                new BankAccount(accounts.get(0), "1234543212345432123454321234", "firstBankAccount", "universitary", Currency.getInstance("EUR"), new Money(BigDecimal.ZERO), new Money(BigDecimal.valueOf(100)), BankAccount.Status.ACTIVE),
                new BankAccount(accounts.get(1), "1234543212311112123454321234", "AeroBankAccount", "boh", Currency.getInstance("EUR"), new Money(BigDecimal.ZERO), new Money(BigDecimal.valueOf(50)), BankAccount.Status.ACTIVE)
        );
        bankAccountRepository.saveAll(bankAccounts);

        Map<String, BigDecimal> currenciesEUR = new HashMap<>();
        currenciesEUR.put("USD", BigDecimal.valueOf(0.8));
        currenciesEUR.put("GBP", BigDecimal.valueOf(1.2));
        currenciesEUR.put("AMD", BigDecimal.valueOf(10.0));
        Map<String, BigDecimal> currenciesUSD = new HashMap<>();
        currenciesUSD.put("EUR", BigDecimal.valueOf(1.2));
        currenciesUSD.put("GBP", BigDecimal.valueOf(1.7));
        currenciesUSD.put("AMD", BigDecimal.valueOf(12.0));
        Map<String, BigDecimal> currenciesGBP = new HashMap<>();
        currenciesGBP.put("USD", BigDecimal.valueOf(1.2));
        currenciesGBP.put("EUR", BigDecimal.valueOf(1.5));
        currenciesGBP.put("AMD", BigDecimal.valueOf(15));
        List<ExchangeCurrencyValueHistory> exchangeCurrencyValueHistories = Arrays.asList(
                new ExchangeCurrencyValueHistory(Instant.ofEpochSecond(1616630403), Instant.ofEpochSecond(1616730403), "EUR", currenciesEUR),
                new ExchangeCurrencyValueHistory(Instant.ofEpochSecond(1616630402), Instant.ofEpochSecond(1616730402), "USD", currenciesUSD),
                new ExchangeCurrencyValueHistory(Instant.ofEpochSecond(1616630401), Instant.ofEpochSecond(1616730401), "GBP", currenciesGBP),
                new ExchangeCurrencyValueHistory(Instant.ofEpochSecond(1615630401), Instant.ofEpochSecond(1615740401), "EUR", currenciesEUR),
                new ExchangeCurrencyValueHistory(Instant.ofEpochSecond(1615630401), Instant.ofEpochSecond(1615740401), "USD", currenciesUSD),
                new ExchangeCurrencyValueHistory(Instant.ofEpochSecond(1615630401), Instant.ofEpochSecond(1615740401), "GBP", currenciesGBP));
        exchangeCurrencyValueHistoryRepository.saveAll(exchangeCurrencyValueHistories);

        Map<String, BigDecimal> currenciesEUR1 = new HashMap<>();
        currenciesEUR.put("USD", BigDecimal.valueOf(0.8));
        currenciesEUR.put("GBP", BigDecimal.valueOf(1.2));
        currenciesEUR.put("AMD", BigDecimal.valueOf(10.0));
        Map<String, BigDecimal> currenciesUSD1 = new HashMap<>();
        currenciesUSD.put("EUR", BigDecimal.valueOf(1.2));
        currenciesUSD.put("GBP", BigDecimal.valueOf(1.7));
        currenciesUSD.put("AMD", BigDecimal.valueOf(12.0));
        Map<String, BigDecimal> currenciesGBP1 = new HashMap<>();
        currenciesGBP.put("USD", BigDecimal.valueOf(1.2));
        currenciesGBP.put("EUR", BigDecimal.valueOf(1.5));
        currenciesGBP.put("AMD", BigDecimal.valueOf(15));
        List<ExchangeCurrencyValue> exchangeCurrencyValues = Arrays.asList(
                new ExchangeCurrencyValue(Instant.now(), Instant.now(), "EUR", currenciesEUR1),
                new ExchangeCurrencyValue(Instant.now(), Instant.now(), "USD", currenciesUSD1),
                new ExchangeCurrencyValue(Instant.now(), Instant.now(), "GBP", currenciesGBP1));
        exchangeCurrencyValueRepository.saveAll(exchangeCurrencyValues);
    }

    public synchronized void clearAll() {
        bankAccountRepository.deleteAll();
        accountRepository.deleteAll();
        exchangeCurrencyValueHistoryRepository.deleteAll();
        exchangeCurrencyValueRepository.deleteAll();

    }


}
