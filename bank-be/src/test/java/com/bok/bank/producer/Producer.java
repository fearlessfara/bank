package com.bok.bank.producer;

import com.bok.parent.integration.message.AccountCreationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Producer {
    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${active-mq.bank-users}")
    private String usersQueue;


    public void send(AccountCreationMessage userCreationMessage) {
        try {
            log.info("Attempting Send user creation message to queue: " + usersQueue);
            jmsTemplate.convertAndSend(usersQueue, userCreationMessage);
        } catch (Exception e) {
            log.error("Received Exception during send Message: ", e);
        }
    }
}
