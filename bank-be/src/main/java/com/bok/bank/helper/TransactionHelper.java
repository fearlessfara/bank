package com.bok.bank.helper;

import com.bok.bank.exception.BankAccountException;
import com.bok.bank.exception.ErrorCode;
import com.bok.bank.exception.TransactionException;
import com.bok.bank.integration.dto.AuthorizationResponseDTO;
import com.bok.bank.integration.dto.TransactionDTO;
import com.bok.bank.integration.dto.TransactionResponseDTO;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.Card;
import com.bok.bank.model.Transaction;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.repository.CardRepository;
import com.bok.bank.repository.TransactionRepository;
import com.bok.bank.util.Money;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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
    @Autowired
    CardRepository cardRepository;

    public AuthorizationResponseDTO authorizeTransaction(Long accountId, Money amount, String fromMarket, String cardToken) {
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findByAccountIdAndStatus(accountId, BankAccount.Status.ACTIVE);
        if (!bankAccountOptional.isPresent())
            return new AuthorizationResponseDTO(false, "User not have a bank account or is bank account not is active", null);
        BankAccount bankAccount = bankAccountOptional.get();
        Money availableBalance = bankAccount.getAvailableAmount().subtract(bankAccount.getBlockedAmount());
        boolean isImportAvailable;
        Card card = cardRepository.findByToken(cardToken).orElse(null);
        if (Objects.isNull(card)) {
            return new AuthorizationResponseDTO(false, "Card not found by token: " + cardToken, null);
        }
        if (!card.getCardStatus().equals(Card.CardStatus.ACTIVE)) {
            return new AuthorizationResponseDTO(false, "Card is not ACTIVE, the status is: " + card.getCardStatus().name(), null);
        }
        if (bankAccount.getCurrency().equals(amount.getCurrency())) {
            isImportAvailable = availableBalance.isGreaterOrEqualsThan(amount);
        } else {
            amount = exchangeCurrencyAmountHelper.convertCurrencyAmount(amount, bankAccount.getCurrency());
            isImportAvailable = availableBalance.isGreaterOrEqualsThan(amount);
        }
        if (isImportAvailable) {
            bankAccount.setBlockedAmount(bankAccount.getBlockedAmount().plus(amount));
            bankAccount.setAvailableAmount(bankAccount.getAvailableAmount().subtract(amount));
            bankAccount = bankAccountRepository.saveAndFlush(bankAccount);
            Transaction transaction = new Transaction(Transaction.Type.PAYMENT, Transaction.Status.AUTHORISED, fromMarket, bankAccount, amount, UUID.randomUUID(), card);
            transaction = transactionRepository.saveAndFlush(transaction);
            return new AuthorizationResponseDTO(true, "", UUID.fromString(transaction.getPublicId()));
        }
        Transaction transaction = new Transaction(Transaction.Type.PAYMENT, Transaction.Status.DECLINED, fromMarket, bankAccount, amount, UUID.randomUUID(), card);
        transaction = transactionRepository.saveAndFlush(transaction);
        return new AuthorizationResponseDTO(false, "Amount not available", UUID.fromString(transaction.getPublicId()));

    }

    @Transactional
    public void performTransaction(TransactionDTO transactionDTO) {
        Preconditions.checkArgument(Objects.nonNull(transactionDTO), "withdrawalDTO passed is null");
        Preconditions.checkArgument(StringUtils.isNotBlank(transactionDTO.fromMarket), "fromMarket passed is blank");
        Preconditions.checkNotNull(transactionDTO.extTransactionId, "extTransactionId passed is null");
        Preconditions.checkArgument(Objects.nonNull(transactionDTO.transactionAmount) && transactionDTO.transactionAmount.amount.compareTo(BigDecimal.ZERO) == 1, "amount not valid");
        BankAccount bankAccount = bankAccountRepository.findByAccountId(transactionDTO.accountId).orElseThrow(BankAccountException::new);
        executeTransaction(transactionDTO, bankAccount);
    }

    public List<TransactionResponseDTO> findTransactionsByAccountId(Long accountId) {
        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
        if (transactions.isEmpty()) {
            return Collections.emptyList();
        }
        return transactions.stream().map(t -> toTransactionResponseDTO(t, accountId)).collect(Collectors.toList());
    }

    public TransactionResponseDTO toTransactionResponseDTO(Transaction transaction, Long accountId) {
        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO(transaction.getPublicId(), transaction.getType().name(), transaction.getStatus().name(), transaction.getTimestamp());
        Money amount = transaction.getAmount();
        if (Objects.nonNull(transaction.getFromBankAccount()) && transaction.getFromBankAccount().getAccountId().equals(accountId)) {
            amount.setValue(amount.getValue().negate());
        }
        transactionResponseDTO.amount = new com.bok.bank.integration.util.Money(amount.getCurrency(), amount.getValue());
        return transactionResponseDTO;
    }

    private void executeTransaction(TransactionDTO transactionDTO, BankAccount bankAccount) {
        Optional<Transaction> transactionOptional = transactionRepository.findByPublicId(transactionDTO.extTransactionId.toString());
        Transaction transaction;
        if (!transactionOptional.isPresent() && transactionDTO.type.equals(Transaction.Type.DEPOSIT.name())) {
            transaction = new Transaction(Transaction.Type.DEPOSIT, Transaction.Status.SETTLED, transactionDTO.fromMarket, bankAccount, new Money(transactionDTO.transactionAmount.amount, transactionDTO.transactionAmount.currency), UUID.randomUUID(), bankAccount.getAccountId());
        } else {
            transaction = transactionOptional.orElseThrow(() -> new TransactionException(ErrorCode.TRANSACTION_NOT_VALID.name()));
        }
        Money amountWithBankAccountCurrency = exchangeCurrencyAmountHelper.convertCurrencyAmount(transaction.getAmount(), bankAccount.getCurrency());
        switch (transaction.getType()) {
            case DEPOSIT: {
                bankAccount.setAvailableAmount(bankAccount.getAvailableAmount().plus(amountWithBankAccountCurrency));
                transaction.setToBankAccount(bankAccount);
                transaction.setFromBankAccount(null);
                break;
            }
            case WITHDRAWAL:
            case PAYMENT: {
                if (transaction.getStatus().equals(Transaction.Status.DECLINED) || transaction.getStatus().equals(Transaction.Status.CANCELLED)) {
                    break;
                }
                bankAccount.setBlockedAmount(bankAccount.getBlockedAmount().subtract(amountWithBankAccountCurrency));
                transaction.setToBankAccount(null);
                transaction.setFromBankAccount(bankAccount);
                transaction.setStatus(Transaction.Status.SETTLED);
                break;
            }
            default: {
                throw new TransactionException(ErrorCode.TRANSACTION_TYPE_NOT_VALID.name());
            }
        }
        transactionRepository.saveAndFlush(transaction);
        bankAccountRepository.saveAndFlush(bankAccount);
    }

    public List<Transaction> findTransactionByCardToken(Long accountId, String token) {
        Card card = cardRepository.findByToken(token).orElseThrow(() -> new IllegalArgumentException("Card with token: " + token + " not found"));
        Preconditions.checkArgument(card.getAccount().getId().equals(accountId), "Card not found for accountId: " + accountId);
        return transactionRepository.findTransactionsByCard_Id(card.getId());
    }
}
