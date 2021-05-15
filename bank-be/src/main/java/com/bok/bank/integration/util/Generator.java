package com.bok.bank.integration.util;

import com.bok.bank.integration.model.ConfirmationEmailHistory;
import com.bok.bank.integration.repository.BankAccountRepository;
import com.bok.bank.integration.repository.ConfirmationEmailHistoryRepository;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Generator {
    @Autowired
    BankAccountRepository bankAccountRepository;
    @Autowired
    ConfirmationEmailHistoryRepository confirmationEmailHistoryRepository;

    @Value("${bank-info.bank-code}")
    private String BANK_CODE;

    public String generateIBAN() {
        Iban iban = new Iban.Builder()
                .countryCode(CountryCode.IT)
                .bankCode(BANK_CODE)
                .buildRandom();
        return bankAccountRepository.existsByIBAN(iban.toFormattedString()) ? generateIBAN() : iban.toFormattedString();
    }

    public String generateConfirmationToken() {
        String confirmationToken = UUID.randomUUID().toString();
        return confirmationEmailHistoryRepository.existsByConfirmationToken(confirmationToken) ? generateConfirmationToken() : confirmationToken;
    }

    public String generateUrlServiceByResourceType(ConfirmationEmailHistory.ResourceType resourceType) {
        switch (resourceType) {
            case CARD:
                return "/card";
            case TRANSACTION:
                return "/transaction";
            case BANK_ACCOUNT:
                return "/bankAccount";
            default:
                throw new IllegalStateException("ResourceType Not found");
        }
    }
}
