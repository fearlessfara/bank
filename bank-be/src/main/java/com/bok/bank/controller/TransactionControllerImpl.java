package com.bok.bank.controller;

import com.bok.bank.helper.TransactionHelper;
import com.bok.bank.integration.dto.TransactionResponseDTO;
import com.bok.bank.integration.service.TransactionController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TransactionControllerImpl implements TransactionController {

    @Autowired
    TransactionHelper transactionHelper;

    @Override
    public List<TransactionResponseDTO> allTransaction(Long accountId) {
        return transactionHelper.findTransactionsByAccountId(accountId);
    }
}
