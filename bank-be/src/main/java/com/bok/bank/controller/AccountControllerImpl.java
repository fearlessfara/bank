package com.bok.bank.controller;

import com.bok.bank.helper.AccountHelper;
import com.bok.bank.integration.dto.AccountInfoDTO;
import com.bok.bank.integration.dto.BankCheckRequestDTO;
import com.bok.bank.integration.service.AccountController;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
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

    @Override
    public Boolean checkCreation(BankCheckRequestDTO bankCheckRequestDTO) {
        Preconditions.checkNotNull(bankCheckRequestDTO.business, "business field is null");
        Preconditions.checkArgument(StringUtils.isNotBlank(bankCheckRequestDTO.fiscalCode) || StringUtils.isNotBlank(bankCheckRequestDTO.vatNumber), "fiscalCode and VatNumber fields are blank");
        return accountHelper.canCreate(bankCheckRequestDTO);
    }

}
