package com.bok.bank.helper;

import com.bok.bank.exception.BankAccountException;
import com.bok.bank.exception.ErrorCode;
import com.bok.bank.exception.TransactionException;
import com.bok.bank.integration.dto.AuthorizationResponseDTO;
import com.bok.bank.integration.dto.TransactionDTO;
import com.bok.bank.integration.dto.TransactionResponseDTO;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public AuthorizationResponseDTO authorizeTransaction(Long accountId, Money amount, String fromMarket) {
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findByAccountIdAndStatus(accountId, BankAccount.Status.ACTIVE);
        if (!bankAccountOptional.isPresent())
            return new AuthorizationResponseDTO(false, "User not have a bank account or is bank account not is active", null);
        BankAccount bankAccount = bankAccountOptional.get();
        Money availableBalance = bankAccount.getAvailableAmount().subtract(bankAccount.getBlockedAmount());
        boolean isImportAvailable;
        if (bankAccount.getCurrency().equals(amount.getCurrency())) {
            isImportAvailable = availableBalance.isGreaterOrEqualsThan(amount);
        } else {
            amount = exchangeCurrencyAmountHelper.convertCurrencyAmount(amount, bankAccount.getCurrency());
            isImportAvailable = availableBalance.isGreaterOrEqualsThan(amount);
        }
        if (isImportAvailable) {
            bankAccount.setBlockedAmount(amount);
            bankAccount = bankAccountRepository.saveAndFlush(bankAccount);
            Transaction transaction = new Transaction(Transaction.Type.WITHDRAWAL, Transaction.Status.AUTHORISED, fromMarket, bankAccount, amount);
            transaction = transactionRepository.saveAndFlush(transaction);
            return new AuthorizationResponseDTO(true, "", transaction.getPublicId());
        }
        Transaction transaction = new Transaction(Transaction.Type.WITHDRAWAL, Transaction.Status.DECLINED, fromMarket, bankAccount, amount);
        transaction = transactionRepository.saveAndFlush(transaction);
        return new AuthorizationResponseDTO(false, "Amount not available", transaction.getPublicId());

    }

    @Transactional
    public void performTransaction(TransactionDTO transactionDTO) {
        Preconditions.checkArgument(Objects.nonNull(transactionDTO), "withdrawalDTO passed is null");
        Preconditions.checkArgument(StringUtils.isNotBlank(transactionDTO.fromMarket), "fromMarket passed is blank");
        Preconditions.checkNotNull(transactionDTO.extTransactionId, "extTransactionId passed is null");
        Preconditions.checkArgument(Objects.nonNull(transactionDTO.transactionAmount) && transactionDTO.transactionAmount.amount.compareTo(BigDecimal.ZERO) == 1, "amount not valid");

        Money amount = new Money(transactionDTO.transactionAmount.amount, transactionDTO.transactionAmount.currency);
        BankAccount toBankAccount = bankAccountRepository.findByAccountId(transactionDTO.accountId).orElseThrow(BankAccountException::new);
        if (!authorizeTransaction(transactionDTO.accountId, amount, transactionDTO.fromMarket).authorized) {
            throw new TransactionException();
        }
        executeTransaction(transactionDTO, toBankAccount);
    }

    public List<TransactionResponseDTO> findTransactionsByAccountId(Long accountId) {
        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
        if (transactions.isEmpty()) {
            return null;
        }
        return transactions.stream().map(t -> toTransactionResponseDTO(t, accountId)).collect(Collectors.toList());
    }

    private TransactionResponseDTO toTransactionResponseDTO(Transaction transaction, Long accountId) {
        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO(transaction.getPublicId().toString(), transaction.getType().name(), transaction.getStatus().name(), transaction.getTimestamp());
        Money amount = transaction.getAmount();
        if (transaction.getFromBankAccount().getAccountId().equals(accountId)) {
            amount.subtract(amount.multiply(2));
        }
        transactionResponseDTO.amount = new com.bok.bank.integration.util.Money(amount.getCurrency(), amount.getValue());
        return transactionResponseDTO;
    }

    private void executeTransaction(TransactionDTO transactionDTO, BankAccount toBankAccount) {
        Optional<Transaction> transactionOptional = transactionRepository.findByPublicId(transactionDTO.extTransactionId);
        Transaction transaction;
        if (!transactionOptional.isPresent() && transactionDTO.type.equals(Transaction.Type.DEPOSIT.name())) {
            transaction = new Transaction(Transaction.Type.DEPOSIT, Transaction.Status.SETTLED, transactionDTO.fromMarket, toBankAccount, new Money(transactionDTO.transactionAmount.amount, transactionDTO.transactionAmount.currency));
        } else {
            transaction = transactionOptional.orElseThrow(() -> new TransactionException(ErrorCode.TRANSACTION_NOT_VALID.name()));
        }
        Money amountWithBankAccountCurrency = exchangeCurrencyAmountHelper.convertCurrencyAmount(transaction.getAmount(), toBankAccount.getCurrency());
        switch (transaction.getType()) {
            case DEPOSIT:
                toBankAccount.setAvailableAmount(toBankAccount.getAvailableAmount().plus(amountWithBankAccountCurrency));
                break;
            case WITHDRAWAL:
                if (transaction.getStatus().equals(Transaction.Status.DECLINED) || transaction.getStatus().equals(Transaction.Status.CANCELLED)) {
                    break;
                }
                toBankAccount.setAvailableAmount(toBankAccount.getAvailableAmount().subtract(amountWithBankAccountCurrency));
                toBankAccount.setBlockedAmount(toBankAccount.getBlockedAmount().plus(amountWithBankAccountCurrency));
                transaction.setStatus(Transaction.Status.SETTLED);
                break;
            default:
                throw new TransactionException(ErrorCode.TRANSACTION_TYPE_NOT_VALID.name());
        }
        transactionRepository.saveAndFlush(transaction);
        bankAccountRepository.saveAndFlush(toBankAccount);
    }
}
