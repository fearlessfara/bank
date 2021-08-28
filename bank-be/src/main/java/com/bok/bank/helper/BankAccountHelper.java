package com.bok.bank.helper;

import com.bok.bank.exception.AccountException;
import com.bok.bank.exception.BankAccountException;
import com.bok.bank.exception.ErrorCode;
import com.bok.bank.integration.dto.BankAccountDTO;
import com.bok.bank.integration.dto.BankAccountInfoDTO;
import com.bok.bank.model.Account;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.ConfirmationEmailHistory;
import com.bok.bank.repository.AccountRepository;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.repository.ConfirmationEmailHistoryRepository;
import com.bok.bank.util.CreditCardNumberGenerator;
import com.bok.bank.util.Generator;
import com.bok.bank.util.Money;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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

    @Value("${bank-info.init-bonus}")
    private Long INIT_BONUS;


    /**
     * This method get the principal bank account info from database
     * @param accountId
     * @return bankAccountInfoDTO
     */
    public BankAccountInfoDTO getBankAccountInfo(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(AccountException::new);
        BankAccount bankAccount = bankAccountRepository.findByAccountId(accountId).orElseThrow(BankAccountException::new);

        return new BankAccountInfoDTO(account.getEmail(), bankAccount.getName(), bankAccount.getIBAN(), bankAccount.getLabel(),
                bankAccount.getCurrency(), bankAccount.getBlockedAmount().getValue(), bankAccount.getAvailableAmount().getValue(), bankAccount.getStatus().name());
    }

    /**
     * In the future we want to handle more bank account for one account and this method create and link a new bankAccount to the user that make a request
     * @param accountId
     * @param bankAccountDTO
     * @return
     */
    @Deprecated
    public String createBankAccount(Long accountId, BankAccountDTO bankAccountDTO) {
        log.info("creation bank account");
        BankAccount bankAccount = new BankAccount(accountId, generator.generateIBAN(), bankAccountDTO.name, bankAccountDTO.label, bankAccountDTO.currency, new Money(BigDecimal.ZERO, bankAccountDTO.currency), new Money(BigDecimal.ZERO, bankAccountDTO.currency), BankAccount.Status.PENDING);
        bankAccount = bankAccountRepository.saveAndFlush(bankAccount);
        Account account = accountRepository.findById(accountId).orElseThrow(AccountException::new);
        account.setBankAccountId(bankAccount.getId());
        account = accountRepository.save(account);
        emailHelper.sendBankAccountConfirmationEmail(account, bankAccount);
        return "Please check your mail and confirm bank account creation";
    }

    /**
     * This method create the first bank account linked to the user that make a subscription
     * @param accountId
     * @param bankAccountDTO
     */
    public void createFirstBankAccount(Long accountId, BankAccountDTO bankAccountDTO) {
        log.info("creation bank account");
        BankAccount bankAccount = new BankAccount(accountId, generator.generateIBAN(), bankAccountDTO.name, bankAccountDTO.label, bankAccountDTO.currency, new Money(BigDecimal.ZERO, bankAccountDTO.currency), new Money(new BigDecimal(INIT_BONUS), bankAccountDTO.currency), BankAccount.Status.ACTIVE);
        bankAccount = bankAccountRepository.saveAndFlush(bankAccount);
        Account account = accountRepository.findById(accountId).orElseThrow(AccountException::new);
        account.setBankAccountId(bankAccount.getId());
        accountRepository.save(account);
    }

    /**
     * In the future we want to handle more bank account for one account and this method verify and active the new bank account created
     * @param accountId
     * @param confirmationToken
     * @return
     */
    @Deprecated
    public BankAccountInfoDTO verifyBankAccount(Long accountId, String confirmationToken) {
        ConfirmationEmailHistory confirmationEmailHistory = confirmationEmailHelper.findAndVerifyConfirmationToken(accountId, confirmationToken, ConfirmationEmailHistory.ResourceType.CARD);
        Preconditions.checkArgument(bankAccountRepository.changeBankAccountStatus(confirmationEmailHistory.getResourceId(), BankAccount.Status.ACTIVE) < 0, "Bank account confirmation is failed, please try again");
        return getBankAccountInfo(accountId);
    }

}
