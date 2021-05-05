package com.bok.bank.helper;

import com.bok.bank.dto.BankAccountInfoDTO;
import com.bok.bank.dto.CheckPaymentAmountResponseDTO;
import com.bok.bank.dto.NewBankAccountDTO;
import com.bok.bank.model.Account;
import com.bok.bank.model.BankAccount;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.util.Money;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BankAccountHelper {

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    ExchangeCurrencyAmountHelper exchangeCurrencyAmountHelper;

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

    public BankAccountInfoDTO createBankAccount(Long accountId, NewBankAccountDTO newBankAccountDTO) {
        return null;
//        BankAccount bankAccount = new BankAccount(new Account(accountId), newBankAccountDTO.name, newBankAccountDTO.name, newBankAccountDTO.currency, Money.ZERO, Money.ZERO, BankAccount.Status.ACTIVE);
    }

    public void checkBankAccountInfoForCreation(Long accountId, NewBankAccountDTO newBankAccountDTO) {
        Preconditions.checkArgument(!bankAccountRepository.existsByAccount_Id(accountId), "This account have already an bank account with us; accountId: " + accountId);
        Preconditions.checkArgument(newBankAccountDTO.name.trim().length()>1, "Name of bank account not valid");
        Preconditions.checkNotNull(newBankAccountDTO.currency, "Cannot create bank account without currency");
    }
}
