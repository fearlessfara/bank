package com.bok.bank.controller;

import com.bok.bank.helper.TransactionHelper;
import com.bok.bank.integration.dto.AuthorizationRequestDTO;
import com.bok.bank.integration.dto.AuthorizationResponseDTO;
import com.bok.bank.integration.dto.TransactionResponseDTO;
import com.bok.bank.integration.service.TransactionController;
import com.bok.bank.util.Money;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bok.bank.util.Constants.UNKNOWN_MARKET;

@Service
@Slf4j
public class TransactionControllerImpl implements TransactionController {

    @Autowired
    TransactionHelper transactionHelper;

    @Override
    public List<TransactionResponseDTO> allTransaction(Long accountId) {
        return transactionHelper.findTransactionsByAccountId(accountId);
    }


    @Override
    public AuthorizationResponseDTO authorize(Long accountId, AuthorizationRequestDTO request) {
        log.info("{} {}", request.money.amount.toString(), request.money.currency);
        Preconditions.checkNotNull(accountId, "accountId is null");
        Preconditions.checkNotNull(request.money, "Money passed is null");
        Preconditions.checkNotNull(request.money.amount, "Amount passed is null");
        Preconditions.checkArgument(request.money.amount.intValue() > 0, "Amount passed is ZERO or negative");
        Preconditions.checkNotNull(request.money.currency, "Currency passed is null");
        request.fromMarket = StringUtils.isBlank(request.fromMarket) ? UNKNOWN_MARKET : request.fromMarket;
        return transactionHelper.authorizeTransaction(accountId, Money.money(request.money.amount, request.money.currency), request.fromMarket);
    }

}
