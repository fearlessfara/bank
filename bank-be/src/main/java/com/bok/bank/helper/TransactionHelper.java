package com.bok.bank.helper;

import com.bok.bank.exception.BankAccountException;
import com.bok.bank.exception.ErrorCode;
import com.bok.bank.exception.TransactionException;
import com.bok.bank.integration.dto.AuthorizationResponseDTO;
import com.bok.bank.integration.dto.TransactionDTO;
import com.bok.bank.model.Account;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.Transaction;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.repository.TransactionRepository;
import com.bok.bank.util.Money;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class TransactionHelper {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    BankAccountHelper bankAccountHelper;
    @Autowired
    BankAccountRepository bankAccountRepository;
    @Autowired
    ExchangeCurrencyAmountHelper exchangeCurrencyAmountHelper;

    public AuthorizationResponseDTO authorizeTransaction(Long accountId, Money amount, String fromMarket){
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findByAccount_IdAndStatus(accountId, BankAccount.Status.ACTIVE);
        if (!bankAccountOptional.isPresent())
            return new AuthorizationResponseDTO(false, "User not have a bank account or is bank account not is active", -1L);
        BankAccount bankAccount = bankAccountOptional.get();
        Money availableBalance = bankAccount.getAvailableAmount().subtract(bankAccount.getBlockedAmount());
        boolean isImportAvailable;
        if (bankAccount.getCurrency().equals(amount.getCurrency())) {
            isImportAvailable = availableBalance.isGreaterOrEqualsThan(amount);
        } else {
            isImportAvailable = availableBalance.isGreaterOrEqualsThan(exchangeCurrencyAmountHelper.convertAmount(amount, availableBalance));
        }
        Long transactionId = -1L;
        if(isImportAvailable){
            bankAccount.setBlockedAmount(amount);
            bankAccount = bankAccountRepository.saveAndFlush(bankAccount);
            Transaction transaction = new Transaction(Transaction.Type.WITHDRAWAL, Transaction.Status.AUTHORISED, fromMarket, bankAccount, amount);
            transaction = transactionRepository.saveAndFlush(transaction);
            transactionId = transaction.getId();
        }
        return isImportAvailable ? new AuthorizationResponseDTO(true, "", transactionId) : new AuthorizationResponseDTO(false, "Amount not available", -1L);

    }

    @Transactional
    public void performTransaction(TransactionDTO transactionDTO) {
        Preconditions.checkArgument(Objects.nonNull(transactionDTO), "withdrawalDTO passed is null");
        Preconditions.checkArgument(StringUtils.isNotBlank(transactionDTO.fromMarket), "fromMarket passed is blank");
        Preconditions.checkNotNull(transactionDTO.transactionId, "transactionId passed is null");
        Preconditions.checkArgument(Objects.nonNull(transactionDTO.transactionAmount) && transactionDTO.transactionAmount.amount.compareTo(BigDecimal.ZERO) == 1, "amount not valid");

        Money amount = new Money(transactionDTO.transactionAmount.amount, transactionDTO.transactionAmount.currency);
        BankAccount toBankAccount = bankAccountRepository.findByAccount_Id(transactionDTO.accountId).orElseThrow(BankAccountException::new);
        if (!authorizeTransaction(transactionDTO.accountId, amount, transactionDTO.fromMarket).authorized) {
            throw new TransactionException();
        }
        executeTransaction(transactionDTO, toBankAccount);
    }

    private void executeTransaction(TransactionDTO transactionDTO, BankAccount toBankAccount) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionDTO.getTransactionId());
        Transaction transaction;
        if(!transactionOptional.isPresent() && transactionDTO.type.equals(Transaction.Type.DEPOSIT.name())){
            transaction = new Transaction(Transaction.Type.DEPOSIT, Transaction.Status.SETTLED, transactionDTO.fromMarket, toBankAccount,new Money(transactionDTO.transactionAmount.amount, transactionDTO.transactionAmount.currency));
        }else {
            transaction = transactionOptional.orElseThrow(() -> new TransactionException(ErrorCode.TRANSACTION_NOT_VALID.name()));
        }
        switch (transaction.getType()) {
            case DEPOSIT:
                toBankAccount.setAvailableAmount(toBankAccount.getAvailableAmount().plus(transaction.getAmount()));
                break;
            case WITHDRAWAL:
                toBankAccount.setAvailableAmount(toBankAccount.getAvailableAmount().subtract(transaction.getAmount()));
                toBankAccount.setBlockedAmount(toBankAccount.getBlockedAmount().subtract(transaction.getAmount()));
                transaction.setStatus(Transaction.Status.SETTLED);
                break;
            default:
                throw new TransactionException(ErrorCode.TRANSACTION_TYPE_NOT_VALID.name());
        }
        transactionRepository.saveAndFlush(transaction);
        bankAccountRepository.saveAndFlush(toBankAccount);
    }
}
