package com.bok.bank.helper;

import com.bok.bank.integration.dto.AccountInfoDTO;
import com.bok.bank.integration.dto.BankAccountDTO;
import com.bok.bank.integration.grpc.AccountCreationCheckRequest;
import com.bok.bank.model.Account;
import com.bok.bank.model.Company;
import com.bok.bank.model.User;
import com.bok.bank.repository.AccountRepository;
import com.bok.bank.util.Constants;
import com.bok.parent.integration.message.AccountCreationMessage;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Currency;

@Component
@Slf4j
public class AccountHelper {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    BankAccountHelper bankAccountHelper;

    /**
     * This method get the principal account info from database
     * @param accountId
     * @return accountInfoDTO
     */
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
        return new AccountInfoDTO(accountInfo.getEmail(), accountInfo.getIcc(), accountInfo.getMobile(), accountInfo.getStatus().name(), accountInfo.getType().name(), fullName.trim().replaceAll(" +", " "));
    }

    @Transactional
    public void createAccount(AccountCreationMessage message) {
        Preconditions.checkNotNull(message, "Message of user creation is null: {}", message);
        if (message.business) {
            Company company = new Company(message.accountId, message.name, message.email, message.mobile, message.icc, Account.Status.ACTIVE, message.country, message.county, message.city,
                    message.postalCode, message.street, message.houseNumber, message.vatNumber);
            accountRepository.saveAndFlush(company);
            log.info("Company saved, with mail: {} and id: {}", message.email, message.accountId);
            bankAccountHelper.createFirstBankAccount(message.accountId, new BankAccountDTO(Constants.BOK_COMPANY_BANK_ACCOUNT, Constants.BASIC_LABEL_BANK_ACCOUNT, Currency.getInstance("EUR")));
            return;
        }
        User user = new User(message.accountId, message.name, message.email, message.mobile, message.icc, Account.Status.ACTIVE, message.country, message.county, message.city, message.postalCode, message.street, message.houseNumber,
                message.middleName, message.surname, User.Gender.valueOf(message.gender), message.fiscalCode, message.birthCity, message.birthCountry, message.birthdate.toInstant());
        accountRepository.saveAndFlush(user);
        log.info("User saved, with mail: {} and id: {}", message.email, message.accountId);
        bankAccountHelper.createFirstBankAccount(message.accountId, new BankAccountDTO(Constants.BOK_BASE_BANK_ACCOUNT, Constants.BASIC_LABEL_BANK_ACCOUNT, Currency.getInstance("EUR")));
    }

    public Boolean canCreate(AccountCreationCheckRequest bankCheckRequest) {
        String code = bankCheckRequest.getBusiness() ? bankCheckRequest.getVatNumber() : bankCheckRequest.getFiscalCode();
        return !accountRepository.existsByVatNumberOrTaxCode(code);
    }
}
