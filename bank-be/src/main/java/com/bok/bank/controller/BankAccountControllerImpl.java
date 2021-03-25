package com.bok.bank.controller;

import com.bok.bank.dto.CheckPaymentAmountRequestDTO;
import com.bok.bank.dto.CheckPaymentAmountResponseDTO;
import com.bok.bank.helper.BankAccountHelper;
import com.bok.bank.service.BankAccountController;
import com.bok.bank.util.Money;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class BankAccountControllerImpl implements BankAccountController {

    @Autowired
    BankAccountHelper bankAccountHelper;

    @Override
    public CheckPaymentAmountResponseDTO checkPaymentAmount(Long userId, CheckPaymentAmountRequestDTO request) {
        log.info(request.amount +" "+ request.currency);
        return bankAccountHelper.isAmountAvailable(userId, Money.money(request.amount, request.currency));
    }
}
