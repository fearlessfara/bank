package com.bok.bank.messaging;

import com.bok.bank.helper.AccountHelper;
import com.bok.integration.AccountCreationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountConsumer {

    @Autowired
    AccountHelper accountHelper;

    @JmsListener(destination = "${active-mq.bank-users}")
    public void userListener(AccountCreationMessage message) {
        log.info("Received Message: " + message.toString());
        accountHelper.createAccount(message);
    }


}
