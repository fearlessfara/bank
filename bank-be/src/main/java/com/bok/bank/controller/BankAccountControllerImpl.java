package com.bok.bank.controller;

import com.bok.bank.helper.BankAccountHelper;
import com.bok.bank.helper.TransactionHelper;
import com.bok.bank.integration.dto.BankAccountInfoDTO;
import com.bok.bank.integration.service.BankAccountController;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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


}
