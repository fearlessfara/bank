package com.bok.bank.controller;

import com.bok.bank.dto.BankAccountInfoDTO;
import com.bok.bank.dto.CheckPaymentAmountRequestDTO;
import com.bok.bank.dto.CheckPaymentAmountResponseDTO;
import com.bok.bank.dto.BankAccountDTO;
import com.bok.bank.helper.BankAccountHelper;
import com.bok.bank.service.BankAccountController;
import com.bok.bank.util.Money;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class BankAccountControllerImpl implements BankAccountController {

    @Autowired
    BankAccountHelper bankAccountHelper;

    @Override
    public CheckPaymentAmountResponseDTO checkPaymentAmount(Long accountId, CheckPaymentAmountRequestDTO request) {
        log.info(request.amount.toString() + " " + request.currency);
        Preconditions.checkNotNull(accountId, "accountId is null");
        Preconditions.checkNotNull(request.amount, "Amount passed is null");
        Preconditions.checkNotNull(request.currency, "Currency passed is null");
        return bankAccountHelper.isAmountAvailable(accountId, Money.money(request.amount, request.currency));
    }

    @Override
    public BankAccountInfoDTO bankAccountInfo(Long accountId) {
        Preconditions.checkNotNull(accountId, "accountId is null");
        return bankAccountHelper.getBankAccountInfo(accountId);
    }

    @Override
    public String createBankAccount(Long accountId, BankAccountDTO bankAccountDTO) {
        bankAccountHelper.checkBankAccountInfoForCreation(accountId, bankAccountDTO);
        return bankAccountHelper.createBankAccount(accountId, bankAccountDTO);
    }

    @Override
    public BankAccountInfoDTO verify(Long accountId, String confirmationToken) {
        Preconditions.checkArgument(StringUtils.isNotBlank(confirmationToken.trim()), "configurationToken is blank");
        return bankAccountHelper.verifyBankAccount(accountId, confirmationToken);
    }


}
