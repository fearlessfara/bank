package com.bok.bank.integration.controller;

import com.bok.bank.integration.dto.BankAccountInfoDTO;
import com.bok.bank.integration.dto.CheckPaymentAmountRequestDTO;
import com.bok.bank.integration.dto.CheckPaymentAmountResponseDTO;
import com.bok.bank.integration.dto.BankAccountDTO;
import com.bok.bank.integration.helper.BankAccountHelper;
import com.bok.bank.integration.service.BankAccountController;
import com.bok.bank.integration.util.Money;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Currency;


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
        Preconditions.checkArgument(request.amount.intValue() > 0, "Amount passed is ZERO or negative");
        Preconditions.checkNotNull(request.currency, "Currency passed is null");
        Preconditions.checkArgument(Currency.getAvailableCurrencies().contains(request.currency), "Currency passed is not valid");
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