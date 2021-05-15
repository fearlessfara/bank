package com.bok.bank.controller;

import com.bok.bank.integration.dto.AccountInfoDTO;
import com.bok.bank.helper.AccountHelper;
import com.bok.bank.integration.service.AccountController;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountControllerImpl implements AccountController {

    @Autowired
    AccountHelper accountHelper;

    @Override
    public AccountInfoDTO profileInfo(Long accountId) {
        Preconditions.checkNotNull(accountId, "accountId is null");
        return accountHelper.getAccountInfo(accountId);
    }

}
