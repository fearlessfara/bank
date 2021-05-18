package com.bok.bank.helper;

import com.bok.bank.integration.dto.TransactionDTO;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.Transaction;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.repository.TransactionRepository;
import com.bok.bank.util.Money;
import com.bok.bank.exception.BankAccountException;
import com.bok.bank.exception.ErrorCode;
import com.bok.bank.exception.TransactionException;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

@Component
@Slf4j
public class TransactionHelper {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    BankAccountHelper bankAccountHelper;
    @Autowired
    BankAccountRepository bankAccountRepository;

    public String performWithdrawal(TransactionDTO transactionDTO) {
        performTransaction(transactionDTO, Transaction.Type.WITHDRAWAL);
        return "withdrawal completed";
    }

    public String performDeposit(TransactionDTO transactionDTO) {
        performTransaction(transactionDTO, Transaction.Type.DEPOSIT);
        return "deposit completed";
    }

    @Transactional
    public void performTransaction(TransactionDTO transactionDTO, Transaction.Type type) {
        Preconditions.checkArgument(Objects.nonNull(transactionDTO), "withdrawalDTO passed is null");
        Preconditions.checkArgument(StringUtils.isNotBlank(transactionDTO.fromMarket), "fromMarket passed is blank");
        Preconditions.checkArgument(Objects.nonNull(transactionDTO.amount) && transactionDTO.amount.compareTo(BigDecimal.ZERO) == 1, "amount not valid");

        Money amount = new Money(transactionDTO.amount, Currency.getInstance(transactionDTO.currency.trim().toUpperCase()));
        BankAccount toBankAccount = bankAccountRepository.findByAccount_Id(transactionDTO.accountId).orElseThrow(BankAccountException::new);
        if (!bankAccountHelper.isAmountAvailable(transactionDTO.accountId, amount).authorized) {
            throw new TransactionException();
        }
        Transaction transaction = new Transaction(type, Transaction.Status.SETTLED, transactionDTO.fromMarket, toBankAccount, amount);
        executeTransaction(transaction);
    }

    private void executeTransaction(Transaction transaction) {
        BankAccount toBankAccount = transaction.getToBankAccount();
        switch (transaction.getType()) {
            case DEPOSIT:
                toBankAccount.setAvailableAmount(toBankAccount.getAvailableAmount().plus(transaction.getAmount()));
                break;
            case WITHDRAWAL:
                toBankAccount.setAvailableAmount(toBankAccount.getAvailableAmount().subtract(transaction.getAmount()));
                break;
            default:
                throw new TransactionException(ErrorCode.TRANSACTION_TYPE_NOT_VALID.name());
        }
        transactionRepository.saveAndFlush(transaction);
        bankAccountRepository.saveAndFlush(toBankAccount);
    }
}
