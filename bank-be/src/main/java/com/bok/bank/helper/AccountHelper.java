package com.bok.bank.helper;

import com.bok.bank.integration.dto.AccountInfoDTO;
import com.bok.bank.model.Account;
import com.bok.bank.model.Company;
import com.bok.bank.model.User;
import com.bok.bank.repository.AccountRepository;
import com.google.common.base.Preconditions;
import com.bok.parent.integration.message.AccountCreationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountHelper {

    @Autowired
    AccountRepository accountRepository;

    public AccountInfoDTO getAccountInfo(Long accountId) {
        AccountRepository.Projection.AccountInfo accountInfo = accountRepository.findAccountInfoByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for accountId " + accountId));
        String fullName;
        log.info(accountInfo.toString());
        if (accountInfo.getType().equals(Account.Type.INDIVIDUAL_USER)) {
            fullName = accountInfo.getName() + ((accountInfo.getMiddleName() != null) ? " " + accountInfo.getMiddleName() : "") + " " + accountInfo.getSurname();
        } else {
            fullName = accountInfo.getName();
        }
        return new AccountInfoDTO(accountInfo.getEmail(), accountInfo.getIcc(), accountInfo.getMobile(), accountInfo.getStatus().name(), accountInfo.getType().name(), fullName);
    }

    public void createAccount(AccountCreationMessage message) {
        Preconditions.checkNotNull(message, "Message of user creation is null: {}", message);
        if (message.business) {
            Company company = new Company(message.accountId, message.name, message.email, message.mobile, message.icc, Account.Status.PENDING, message.country, message.county, message.city,
                    message.postalCode, message.street, message.houseNumber, message.vatNumber);
            accountRepository.save(company);
            log.info("Company saved, with mail: {} and id: {}", message.email, message.accountId);
            return;
        }
        User user = new User(message.accountId, message.name, message.email, message.mobile, message.icc, Account.Status.ACTIVE, message.country, message.county, message.city, message.postalCode, message.street, message.houseNumber,
                message.middleName, message.surname, User.Gender.valueOf(message.gender), message.fiscalCode, message.birthCity, message.birthCountry, message.birthdate.toInstant());
        log.info("User saved, with mail: {} and id: {}", message.email, message.accountId);
        accountRepository.save(user);
    }
}
