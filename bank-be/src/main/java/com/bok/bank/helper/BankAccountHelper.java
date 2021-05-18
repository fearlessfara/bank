package com.bok.bank.helper;

import com.bok.bank.integration.dto.BankAccountDTO;
import com.bok.bank.integration.dto.BankAccountInfoDTO;
import com.bok.bank.integration.dto.AuthorizationResponseDTO;
import com.bok.bank.model.Account;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.ConfirmationEmailHistory;
import com.bok.bank.repository.AccountRepository;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.repository.ConfirmationEmailHistoryRepository;
import com.bok.bank.util.CreditCardNumberGenerator;
import com.bok.bank.util.Generator;
import com.bok.bank.util.Money;
import com.bok.bank.exception.AccountException;
import com.bok.bank.exception.BankAccountException;
import com.bok.bank.exception.ErrorCode;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@Slf4j
public class BankAccountHelper {


    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ConfirmationEmailHistoryRepository confirmationEmailHistoryRepository;

    @Autowired
    CreditCardNumberGenerator creditCardNumberGenerator;

    @Autowired
    ExchangeCurrencyAmountHelper exchangeCurrencyAmountHelper;

    @Autowired
    EmailHelper emailHelper;
    @Autowired
    ConfirmationEmailHelper confirmationEmailHelper;

    @Autowired
    Generator generator;

    @Value("${bank-info.bank-code}")
    private String BANK_CODE;

    public AuthorizationResponseDTO isAmountAvailable(Long accountId, Money amount) {
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findByAccount_IdAndStatus(accountId, BankAccount.Status.ACTIVE);
        if (!bankAccountOptional.isPresent())
            return new AuthorizationResponseDTO(false, "User not have a bank account or is bank account not is active");
        BankAccount bankAccount = bankAccountOptional.get();
        Money availableBalance = bankAccount.getAvailableAmount().subtract(bankAccount.getBlockedAmount());
        boolean isImportAvailable;
        if (bankAccount.getCurrency().equals(amount.getCurrency())) {
            isImportAvailable = availableBalance.isGreaterOrEqualsThan(amount);
        } else {
            isImportAvailable = availableBalance.isGreaterOrEqualsThan(exchangeCurrencyAmountHelper.convertAmount(amount, availableBalance));
        }
        return (isImportAvailable) ? new AuthorizationResponseDTO(true, "") : new AuthorizationResponseDTO(false, "Amount not available");
    }

    public BankAccountInfoDTO getBankAccountInfo(Long accountId) {
        BankAccount bankAccount = bankAccountRepository.findByAccount_Id(accountId).orElseThrow(BankAccountException::new);

        return new BankAccountInfoDTO(bankAccount.getAccount().getEmail(), bankAccount.getName(), bankAccount.getIBAN(), bankAccount.getLabel(),
                bankAccount.getCurrency(), bankAccount.getBlockedAmount().getValue(), bankAccount.getAvailableAmount().getValue(), bankAccount.getStatus().name());
    }

    public String createBankAccount(Long accountId, BankAccountDTO bankAccountDTO) {
        log.info("creation bank account");
        BankAccount bankAccount = new BankAccount(new Account(accountId), generator.generateIBAN(), bankAccountDTO.name, bankAccountDTO.label, bankAccountDTO.currency, new Money(BigDecimal.ZERO, bankAccountDTO.currency), new Money(BigDecimal.ZERO, bankAccountDTO.currency), BankAccount.Status.PENDING);
        bankAccount = bankAccountRepository.saveAndFlush(bankAccount);
        Account account = accountRepository.findById(accountId).orElseThrow(AccountException::new);
        account.setBankAccount(bankAccount);
        account = accountRepository.save(account);
        emailHelper.sendBankAccountConfirmationEmail(account, bankAccount);
        return "Please check your mail and confirm bank account creation";
    }
    public void createFirstBankAccount(Long accountId, BankAccountDTO bankAccountDTO) {
        log.info("creation bank account");
        BankAccount bankAccount = new BankAccount(new Account(accountId), generator.generateIBAN(), bankAccountDTO.name, bankAccountDTO.label, bankAccountDTO.currency, new Money(BigDecimal.ZERO, bankAccountDTO.currency), new Money(BigDecimal.valueOf(100), bankAccountDTO.currency), BankAccount.Status.ACTIVE);
        bankAccount = bankAccountRepository.saveAndFlush(bankAccount);
        Account account = accountRepository.findById(accountId).orElseThrow(AccountException::new);
        account.setBankAccount(bankAccount);
        accountRepository.save(account);
    }

    public BankAccountInfoDTO verifyBankAccount(Long accountId, String confirmationToken) {
        ConfirmationEmailHistory confirmationEmailHistory = confirmationEmailHelper.findAndVerifyConfirmationToken(accountId, confirmationToken, ConfirmationEmailHistory.ResourceType.CARD);
        Preconditions.checkArgument(bankAccountRepository.changeBankAccountStatus(confirmationEmailHistory.getResourceId(), BankAccount.Status.ACTIVE) < 0, "Bank account confirmation is failed, please try again");
        return getBankAccountInfo(accountId);
    }

    public void checkBankAccountInfoForCreation(Long accountId, BankAccountDTO bankAccountDTO) {
        Preconditions.checkArgument(!bankAccountRepository.existsBankAccountNotDeletedByAccountId(accountId), ErrorCode.ACCOUNT_ALREADY_HAVE_A_BANK_ACCOUNT);
        Preconditions.checkArgument(bankAccountDTO.name.trim().length() > 1, "Name of bank account not valid");
        Preconditions.checkNotNull(bankAccountDTO.currency, "Cannot create bank account without currency");
    }

}
