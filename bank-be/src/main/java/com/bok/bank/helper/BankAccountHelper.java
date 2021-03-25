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

    public CheckPaymentAmountResponseDTO isAmountAvailable(Long userId, Money amount){
        Optional<BankAccount> bankAccount = bankAccountRepository.findByAccount_Id(userId);
        if(!bankAccount.isPresent())
            return new CheckPaymentAmountResponseDTO(false, "User not have a bank account");
        Money availableBalance = bankAccount.get().getAvailableAmount().subtract(bankAccount.get().getBlockedAmount());
        return new CheckPaymentAmountResponseDTO(availableBalance.isGreaterOrEqualsThan(amount), "");
    }

}
