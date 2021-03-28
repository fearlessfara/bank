package com.bok.bank.helper;

import com.bok.bank.dto.CheckPaymentAmountResponseDTO;
import com.bok.bank.model.BankAccount;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.util.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BankAccountHelper {

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    ExchangeCurrencyAmountHelper exchangeCurrencyAmountHelper;

    public CheckPaymentAmountResponseDTO isAmountAvailable(Long accountId, Money amount){
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findByAccount_Id(accountId);
        if(!bankAccountOptional.isPresent())
            return new CheckPaymentAmountResponseDTO(false, "User not have a bank account");
        BankAccount bankAccount = bankAccountOptional.get();
        Money availableBalance = bankAccount.getAvailableAmount().subtract(bankAccount.getBlockedAmount());
        if(bankAccount.getCurrency().equals(amount.getCurrency())){
            return new CheckPaymentAmountResponseDTO(availableBalance.isGreaterOrEqualsThan(amount), "");
        }
        return new CheckPaymentAmountResponseDTO(availableBalance.isGreaterOrEqualsThan(exchangeCurrencyAmountHelper.convertAmount(amount, availableBalance)), "");

    }

}
