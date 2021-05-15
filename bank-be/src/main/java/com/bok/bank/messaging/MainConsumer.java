package com.bok.bank.messaging;

import com.bok.bank.helper.AccountHelper;
import com.bok.bank.model.Account;
import com.bok.bank.model.BankAccount;
import com.bok.bank.repository.AccountRepository;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.repository.CardRepository;
import com.bok.bank.repository.ConfirmationEmailHistoryRepository;
import com.bok.bank.repository.TransactionRepository;
import com.bok.bank.util.exception.AccountException;
import com.bok.bank.util.exception.BankAccountException;
import com.bok.parent.integration.message.AccountCreationMessage;
import com.bok.parent.integration.message.AccountDeletionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MainConsumer {

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

    @JmsListener(destination = "${active-mq.bank-account-deletion}")
    public void deleteUserListener(AccountDeletionMessage message) {
        log.info("Received Message: " + message.toString());
        Account account = accountRepository.findById(message.accountId).orElseThrow(AccountException::new);
        BankAccount bankAccount = bankAccountRepository.findByAccount_Id(account.getId()).orElseThrow(BankAccountException::new);
        confirmationEmailHistoryRepository.deleteByAccount(account);
        transactionRepository.deleteByFromBankAccount(bankAccount);
        cardRepository.deleteByAccount(account);
        bankAccountRepository.deleteById(bankAccount.getId());
        accountRepository.deleteById(account.getId());
    }
}
