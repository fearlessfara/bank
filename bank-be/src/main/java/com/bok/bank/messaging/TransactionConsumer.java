package com.bok.bank.messaging;

import com.bok.bank.helper.TransactionHelper;
import com.bok.bank.integration.dto.TransactionDTO;
import com.bok.bank.integration.message.BankDepositMessage;
import com.bok.bank.integration.message.BankWithdrawalMessage;
import com.bok.bank.model.Transaction;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class TransactionConsumer {

    @Autowired
    TransactionHelper transactionHelper;

    @JmsListener(destination = "${active-mq.bank-withdrawal}")
    public void paymentListener(BankWithdrawalMessage message) {
        Preconditions.checkArgument(Objects.nonNull(message), "withdrawalDTO passed is null");
        log.info("Received withdrawal Message: " + message);
        Preconditions.checkArgument(StringUtils.isNotBlank(message.fromMarket), "fromMarket passed is blank");
        Preconditions.checkNotNull(message.extTransactionId, "extTransactionId passed is null");
        Preconditions.checkNotNull(message.accountId, "accountId passed is null");
        transactionHelper.performTransaction(new TransactionDTO(message.money, message.accountId, message.fromMarket, message.extTransactionId.toString(), Transaction.Type.PAYMENT.name()));
    }

    @JmsListener(destination = "${active-mq.bank-deposit}")
    public void depositListener(BankDepositMessage message) {
        Preconditions.checkArgument(Objects.nonNull(message), "BankDepositMessage passed is null");
        log.info("Received deposit Message: " + message);
        Preconditions.checkNotNull(message.accountId, "accountId passed is null");
        transactionHelper.performTransaction(new TransactionDTO(message.money, message.accountId, message.fromMarket, Transaction.Type.DEPOSIT.name()));
    }

}
