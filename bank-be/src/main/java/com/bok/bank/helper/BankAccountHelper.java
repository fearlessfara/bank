package com.bok.bank.helper;

import com.bok.bank.dto.BankAccountInfoDTO;
import com.bok.bank.dto.CheckPaymentAmountResponseDTO;
import com.bok.bank.dto.BankAccountDTO;
import com.bok.bank.model.Account;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.Company;
import com.bok.bank.model.ConfirmationEmailHistory;
import com.bok.bank.model.User;
import com.bok.bank.repository.AccountRepository;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.util.CreditCardNumberGenerator;
import com.bok.bank.util.Generator;
import com.bok.bank.util.Money;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class BankAccountHelper {


    @Autowired
    BankAccountRepository bankAccountRepository;
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CreditCardNumberGenerator creditCardNumberGenerator;

    @Autowired
    ExchangeCurrencyAmountHelper exchangeCurrencyAmountHelper;

    @Autowired
    EmailHelper emailHelper;

    @Autowired
    Generator generator;

    @Value("${bank-info.bank-code}")
    private String BANK_CODE;

    public CheckPaymentAmountResponseDTO isAmountAvailable(Long accountId, Money amount) {
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findByAccount_IdAndStatus(accountId, BankAccount.Status.ACTIVE);
        if (!bankAccountOptional.isPresent())
            return new CheckPaymentAmountResponseDTO(false, "User not have a bank account or is bank account not is active");
        BankAccount bankAccount = bankAccountOptional.get();
        Money availableBalance = bankAccount.getAvailableAmount().subtract(bankAccount.getBlockedAmount());
        boolean isImportAvailable;
        if (bankAccount.getCurrency().equals(amount.getCurrency())) {
            isImportAvailable = availableBalance.isGreaterOrEqualsThan(amount);
        } else {
            isImportAvailable = availableBalance.isGreaterOrEqualsThan(exchangeCurrencyAmountHelper.convertAmount(amount, availableBalance));
        }
        return (isImportAvailable) ? new CheckPaymentAmountResponseDTO(true, "") : new CheckPaymentAmountResponseDTO(false, "Amount not available");
    }

    public BankAccountInfoDTO getBankAccountInfo(Long accountId) {
        BankAccount bankAccount = bankAccountRepository.findByAccount_Id(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Not found bankAccount for accountId " + accountId));
        return new BankAccountInfoDTO(bankAccount.getAccount().getEmail(), bankAccount.getName(), bankAccount.getIBAN(), bankAccount.getLabel(),
                bankAccount.getCurrency(), bankAccount.getBlockedAmount().getValue(), bankAccount.getAvailableAmount().getValue(), bankAccount.getStatus().name());
    }

    public String createBankAccount(Long accountId, BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount = new BankAccount(new Account(accountId), generator.generateIBAN(), bankAccountDTO.name, bankAccountDTO.label, bankAccountDTO.currency, new Money(BigDecimal.ZERO, bankAccountDTO.currency), new Money(BigDecimal.ZERO, bankAccountDTO.currency), BankAccount.Status.PENDING);
        bankAccount = bankAccountRepository.save(bankAccount);
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new IllegalStateException("account not count for accountId: " + accountId));
        account.setBankAccount(bankAccount);
        account = accountRepository.save(account);
        emailHelper.sendAccountConfirmationEmail(account, bankAccount.getId(), ConfirmationEmailHistory.ResourceType.BANK_ACCOUNT);
        return "Please check your mail and confirm bank account creation";
    }

    public void checkBankAccountInfoForCreation(Long accountId, BankAccountDTO bankAccountDTO) {
        Preconditions.checkArgument(!bankAccountRepository.existsBankAccountNotDeletedByAccountId(accountId), "This account have already a bank account with us; accountId: " + accountId);
        Preconditions.checkArgument(bankAccountDTO.name.trim().length() > 1, "Name of bank account not valid");
        Preconditions.checkNotNull(bankAccountDTO.currency, "Cannot create bank account without currency");
    }

    private void updateAccountAndSandEmail(Long accountId, BankAccount bankAccount) {
        if (accountRepository.findAccountTypeById(accountId).equals(Account.Type.INDIVIDUAL_USER)) {
            User account = (User) accountRepository.findById(accountId).orElseThrow(() -> new IllegalStateException("account not count for accountId: " + accountId));
            account.setBankAccount(bankAccount);
            account = accountRepository.save(account);
            emailHelper.sendAccountConfirmationEmail(account, bankAccount.getId(), ConfirmationEmailHistory.ResourceType.BANK_ACCOUNT);
        } else {
            Company account = (Company) accountRepository.findById(accountId).orElseThrow(() -> new IllegalStateException("account not count for accountId: " + accountId));
            account.setBankAccount(bankAccount);
            account = accountRepository.save(account);
            emailHelper.sendAccountConfirmationEmail(account, bankAccount.getId(), ConfirmationEmailHistory.ResourceType.BANK_ACCOUNT);
        }
    }


}
