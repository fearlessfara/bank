package com.bok.bank.helper;

import com.bok.bank.exception.AccountException;
import com.bok.bank.exception.BankAccountException;
import com.bok.bank.exception.ErrorCode;
import com.bok.bank.exception.TransactionException;
import com.bok.bank.integration.dto.AuthorizationResponseDTO;
import com.bok.bank.integration.dto.TransactionDTO;
import com.bok.bank.integration.dto.TransactionResponseDTO;
import com.bok.bank.integration.dto.WireTransferResponseDTO;
import com.bok.bank.model.Account;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.Card;
import com.bok.bank.model.Transaction;
import com.bok.bank.repository.AccountRepository;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.repository.CardRepository;
import com.bok.bank.repository.TransactionRepository;
import com.bok.bank.util.Money;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.bok.bank.model.Transaction.Type.DEPOSIT;
import static com.bok.bank.model.Transaction.Type.WIRE_TRANSFER;

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
    @Autowired
    AccountRepository accountRepository;

    /**
     * given requested param this method check the amount available and the card of the bank account and authorize or decline the payment
     * @param accountId
     * @param amount
     * @param fromMarket
     * @param cardToken
     * @return
     */
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

    /**
     * given requested param this method check the amount available and the IBAN of the bank account and authorize or decline the wire transfer
     * @param transactionDTO
     * @return
     */
    public WireTransferResponseDTO authorizeWireTransfer(TransactionDTO transactionDTO) {
        Account account = accountRepository.findById(transactionDTO.accountId).orElseThrow(AccountException::new);
        BankAccount bankAccount = bankAccountRepository.findByAccountIdAndStatus(transactionDTO.accountId, BankAccount.Status.ACTIVE).orElseThrow(BankAccountException::new);
        if(bankAccount.getIBAN().equals(transactionDTO.destinationIBAN)){
            throw new IllegalStateException("CANNOT DO A WIRE TRANSFER TO YOURSELF");
        }
        Money amount = new Money(transactionDTO.transactionAmount.amount, transactionDTO.transactionAmount.currency);
        Money amountWithBankAccountCurrency = exchangeCurrencyAmountHelper.convertCurrencyAmount(amount, bankAccount.getCurrency());
        if (bankAccount.getAvailableAmount().isGreaterOrEqualsThan(amountWithBankAccountCurrency)) {
            bankAccount.setBlockedAmount(bankAccount.getBlockedAmount().plus(amountWithBankAccountCurrency));
            bankAccount.setAvailableAmount(bankAccount.getAvailableAmount().subtract(amountWithBankAccountCurrency));
            bankAccount = bankAccountRepository.saveAndFlush(bankAccount);
            Transaction transaction = new Transaction(WIRE_TRANSFER, Transaction.Status.AUTHORISED, transactionDTO.destinationIBAN, transactionDTO.causal, transactionDTO.beneficiary, transactionDTO.instantTransfer, transactionDTO.executionDate, bankAccount, null, account, new Money(transactionDTO.transactionAmount.amount, transactionDTO.transactionAmount.currency), UUID.randomUUID());
            transactionRepository.saveAndFlush(transaction);
            return new WireTransferResponseDTO(true, "");
        }
        Transaction transaction = new Transaction(WIRE_TRANSFER, Transaction.Status.DECLINED, transactionDTO.destinationIBAN, transactionDTO.causal, transactionDTO.beneficiary, transactionDTO.instantTransfer, transactionDTO.executionDate, bankAccount, null, account, new Money(transactionDTO.transactionAmount.amount, transactionDTO.transactionAmount.currency), UUID.randomUUID());
        transactionRepository.saveAndFlush(transaction);
        return new WireTransferResponseDTO(false, "Amount not available");

    }


    @Transactional
    public boolean performTransaction(TransactionDTO transactionDTO) {
        Preconditions.checkArgument(Objects.nonNull(transactionDTO), "transactionDTO passed is null");
        Preconditions.checkArgument(Objects.nonNull(transactionDTO.transactionAmount) && transactionDTO.transactionAmount.amount.compareTo(BigDecimal.ZERO) == 1, "amount not valid");
        BankAccount bankAccount = bankAccountRepository.findByAccountId(transactionDTO.accountId).orElseThrow(BankAccountException::new);
        if(bankAccount.getIBAN().equals(transactionDTO.destinationIBAN)){
            throw new IllegalStateException("CANNOT DO A WIRE TRANSFER TO YOURSELF");
        }
        executeTransaction(transactionDTO, bankAccount);
        return true;
    }

    public List<TransactionResponseDTO> findTransactionsByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(AccountException::new);
        BankAccount bankAccount  = bankAccountRepository.findByAccountId(accountId).orElseThrow(BankAccountException::new);
        List<Transaction> transactions = transactionRepository.findDistinctByTransactionOwnerOrFromBankAccountOrToBankAccountOrderByTimestampDesc(account, bankAccount, bankAccount);
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

    /**
     * this method subtract or add the amount of the transactions allowed and after move the transaction in the settled status
     * @param transactionDTO
     * @param bankAccount
     */
    private void executeTransaction(TransactionDTO transactionDTO, BankAccount bankAccount) {
        Transaction transaction;
        switch (Transaction.Type.valueOf(transactionDTO.type)) {
            case DEPOSIT: {
                transaction = new Transaction(DEPOSIT, Transaction.Status.SETTLED, transactionDTO.fromMarket, bankAccount, new Money(transactionDTO.transactionAmount.amount, transactionDTO.transactionAmount.currency), UUID.randomUUID(), transactionDTO.accountId);
                Money amountWithBankAccountCurrency = exchangeCurrencyAmountHelper.convertCurrencyAmount(transaction.getAmount(), bankAccount.getCurrency());
                bankAccount.setAvailableAmount(bankAccount.getAvailableAmount().plus(amountWithBankAccountCurrency));
                transaction.setToBankAccount(bankAccount);
                transaction.setFromBankAccount(null);
                break;
            }
            case WITHDRAWAL:
            case PAYMENT: {
                transaction = transactionRepository.findByPublicId(transactionDTO.extTransactionId.toString()).orElseThrow(() -> new TransactionException(ErrorCode.TRANSACTION_NOT_VALID.name()));
                Money amountWithBankAccountCurrency = exchangeCurrencyAmountHelper.convertCurrencyAmount(transaction.getAmount(), bankAccount.getCurrency());
                if (transaction.getStatus().equals(Transaction.Status.DECLINED) || transaction.getStatus().equals(Transaction.Status.CANCELLED)) {
                    break;
                }
                Card card = transaction.getCard();
                if(Objects.nonNull(card) && card.getType().equals(Card.Type.ONE_USE)) {
                    card.setCardStatus(Card.CardStatus.DESTROYED);
                    cardRepository.saveAndFlush(card);
                }
                bankAccount.setBlockedAmount(bankAccount.getBlockedAmount().subtract(amountWithBankAccountCurrency));
                transaction.setToBankAccount(null);
                transaction.setFromBankAccount(bankAccount);
                transaction.setStatus(Transaction.Status.SETTLED);
                break;
            }
            case WIRE_TRANSFER: {
                Account account = accountRepository.findById(transactionDTO.accountId).orElseThrow(AccountException::new);
                Optional<BankAccount> bankAccountDestinationOptional = bankAccountRepository.findBankAccountByIBAN(transactionDTO.destinationIBAN);
                Money amount = new Money(transactionDTO.transactionAmount.amount, transactionDTO.transactionAmount.currency);
                Money amountWithBankAccountCurrency = exchangeCurrencyAmountHelper.convertCurrencyAmount(amount, bankAccount.getCurrency());
                if (transactionDTO.instantTransfer) {
                    transactionDTO.executionDate = LocalDate.now();
                    if(bankAccount.getAvailableAmount().isLessThan(amount)){
                        throw new TransactionException();
                    }
                    transaction = new Transaction(WIRE_TRANSFER, Transaction.Status.SETTLED, transactionDTO.destinationIBAN, transactionDTO.causal, transactionDTO.beneficiary, transactionDTO.instantTransfer, transactionDTO.executionDate, bankAccount, null, account, new Money(transactionDTO.transactionAmount.amount, transactionDTO.transactionAmount.currency), UUID.randomUUID());
                    if (bankAccountDestinationOptional.isPresent()) {
                        BankAccount bankAccountDestination = bankAccountDestinationOptional.get();
                        transaction.setToBankAccount(bankAccountDestination);
                        Money amountWithBankDestinationCurrency = exchangeCurrencyAmountHelper.convertCurrencyAmount(amount, bankAccountDestination.getCurrency());
                        bankAccountDestination.setAvailableAmount(bankAccountDestination.getAvailableAmount().plus(amountWithBankDestinationCurrency));
                        bankAccountRepository.saveAndFlush(bankAccountDestination);
                    }
                    bankAccount.setAvailableAmount(bankAccount.getAvailableAmount().subtract(amountWithBankAccountCurrency));
                } else {
                    transaction = transactionRepository.findByPublicId(transactionDTO.extTransactionId.toString()).orElseThrow(() -> new TransactionException(ErrorCode.TRANSACTION_NOT_VALID.name()));
                    if (transaction.getStatus().equals(Transaction.Status.DECLINED) || transaction.getStatus().equals(Transaction.Status.CANCELLED)) {
                        break;
                    }
                    transaction.setStatus(Transaction.Status.SETTLED);
                    if (bankAccountDestinationOptional.isPresent()) {
                        BankAccount bankAccountDestination = bankAccountDestinationOptional.get();
                        transaction.setToBankAccount(bankAccountDestination);
                        Money amountWithBankDestinationCurrency = exchangeCurrencyAmountHelper.convertCurrencyAmount(amount, bankAccountDestination.getCurrency());
                        bankAccountDestination.setAvailableAmount(bankAccountDestination.getAvailableAmount().plus(amountWithBankDestinationCurrency));
                        bankAccountRepository.saveAndFlush(bankAccountDestination);
                    }
                    bankAccount.setBlockedAmount(bankAccount.getBlockedAmount().plus(amountWithBankAccountCurrency));
                }

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


    //every day at 8 a.m. process the transfers scheduled for today
    @Scheduled(cron = "0 0 8 * * *")
    public void processScheduledTransfers() {
        List<Transaction> wireTransfers = transactionRepository.findTransactionsByStatusAndTypeAndExecutionDate(Transaction.Status.AUTHORISED, WIRE_TRANSFER, LocalDate.now());
        wireTransfers.forEach(transaction -> {
            performTransaction(new TransactionDTO(new com.bok.bank.integration.util.Money(transaction.getAmount().getCurrency(), transaction.getAmount().getValue()), transaction.getTransactionOwner().getId(), transaction.getDestinationIban(), transaction.getCausal(), transaction.getBeneficiary(), transaction.getInstantTransfer(), transaction.getExecutionDate(), WIRE_TRANSFER.name()));
        });
    }
}
