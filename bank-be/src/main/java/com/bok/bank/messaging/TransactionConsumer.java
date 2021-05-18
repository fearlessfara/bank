package com.bok.bank.messaging;

import com.bok.bank.helper.TransactionHelper;
import com.bok.bank.integration.dto.TransactionDTO;
import com.bok.bank.integration.message.BankWithdrawalMessage;
import com.bok.bank.integration.util.Money;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionConsumer {

    @Autowired
    TransactionHelper transactionHelper;

    @JmsListener(destination = "${active-mq.bank-withdrawal}")
    public void withdrawalListener(BankWithdrawalMessage message) {
        log.info("Received withdrawal Message: " + message.toString());
        transactionHelper.performWithdrawal(new TransactionDTO(message.money, message.accountId, message.fromMarket));
    }

    @JmsListener(destination = "${active-mq.bank-deposit}")
    public void depositListener(BankWithdrawalMessage message) {
        log.info("Received deposit Message: " + message.toString());
        transactionHelper.performDeposit(new TransactionDTO(message.money, message.accountId, message.fromMarket));
    }

}
