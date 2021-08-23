package com.bok.bank.controller;

import com.bok.bank.helper.BankAccountHelper;
import com.bok.bank.helper.TransactionHelper;
import com.bok.bank.integration.dto.AuthorizationRequestDTO;
import com.bok.bank.integration.dto.AuthorizationResponseDTO;
import com.bok.bank.integration.dto.BankAccountDTO;
import com.bok.bank.integration.dto.BankAccountInfoDTO;
import com.bok.bank.integration.service.BankAccountController;
import com.bok.bank.util.Money;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.bok.bank.util.Constants.UNKNOWN_MARKET;


@Service
@Slf4j
public class BankAccountControllerImpl implements BankAccountController {

    @Autowired
    BankAccountHelper bankAccountHelper;
    @Autowired
    TransactionHelper transactionHelper;

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
