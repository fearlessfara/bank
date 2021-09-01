package com.bok.bank.messaging;

import com.bok.bank.exception.AccountException;
import com.bok.bank.exception.BankAccountException;
import com.bok.bank.helper.AccountHelper;
import com.bok.bank.integration.dto.WireTransferRequestDTO;
import com.bok.bank.integration.service.TransactionController;
import com.bok.bank.model.Account;
import com.bok.bank.model.BankAccount;
import com.bok.bank.repository.AccountRepository;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.repository.CardRepository;
import com.bok.bank.repository.ConfirmationEmailHistoryRepository;
import com.bok.bank.repository.TransactionRepository;
import com.bok.bank.util.Money;
import com.bok.parent.integration.message.AccountClosureMessage;
import com.bok.parent.integration.message.AccountCreationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Component
@Slf4j
public class AccountConsumer {

    @Autowired
    AccountHelper accountHelper;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    BankAccountRepository bankAccountRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ConfirmationEmailHistoryRepository confirmationEmailHistoryRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionController transactionController;

    @JmsListener(destination = "${active-mq.bank-account-deletion}")
    @Transactional
    public void deleteUserListener(AccountClosureMessage message) {
        log.info("Received Message: " + message.toString());
        Account account = accountRepository.findById(message.accountId).orElseThrow(AccountException::new);
        BankAccount bankAccount = bankAccountRepository.findByAccountId(account.getId()).orElseThrow(BankAccountException::new);
        if (bankAccount.getAvailableAmount().isGreaterThan(Money.ZERO)) {
            log.warn("The account {} contain an available amount: {} and blocked: {}", message.accountId, bankAccount.getAvailableAmount(), bankAccount.getBlockedAmount());
            transactionController.wireTransfer(message.accountId, new WireTransferRequestDTO(message.iban, account.getName(), "Account Closure",
                    new com.bok.bank.integration.util.Money(bankAccount.getAvailableAmount().getCurrency(), bankAccount.getAvailableAmount().getValue()), LocalDate.now(), true));
        }
        confirmationEmailHistoryRepository.deleteByAccount(account);
        cardRepository.deleteByAccount(account);
        transactionRepository.deleteByTransactionOwnerOrFromBankAccountOrToBankAccount(account, bankAccount, bankAccount);
        bankAccountRepository.deleteById(bankAccount.getId());
        accountRepository.deleteById(account.getId());
    }

    @JmsListener(destination = "${active-mq.bank-users}")
    public void userListener(AccountCreationMessage message) {
        log.info("Received Message: " + message.toString());
        accountHelper.createAccount(message);
    }


}
