package com.bok.bank.helper;

import com.bok.bank.messaging.EmailMQProducer;
import com.bok.bank.model.Account;
import com.bok.bank.model.ConfirmationEmailHistory;
import com.bok.bank.repository.ConfirmationEmailHistoryRepository;
import com.bok.bank.util.Generator;
import com.bok.integration.EmailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailHelper {

    @Autowired
    Generator generator;
    @Autowired
    EmailMQProducer emailMQProducer;
    @Autowired
    ConfirmationEmailHistoryRepository confirmationEmailHistoryRepository;
    @Value("${server.base-url}")
    String baseUrl;


    public void sendAccountConfirmationEmail(Account account, Long resourceId, ConfirmationEmailHistory.ResourceType resourceType) {
        ConfirmationEmailHistory confirmationEmailHistory = new ConfirmationEmailHistory(account, resourceId, resourceType, generator.generateConfirmationToken());
        confirmationEmailHistory = confirmationEmailHistoryRepository.save(confirmationEmailHistory);
        log.info("email: {}", account.getEmail());
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.to = account.getEmail();
        emailMessage.subject = "BOK Bank Account Verification";
        emailMessage.text = "Click on the following link to complete the bank account creation: \n" +
                baseUrl + generator.generateUrlServiceByResourceType(resourceType)+
                "/verify?accountId=" + account.getId() + "&verificationToken=" + confirmationEmailHistory.getConfirmationToken() +
                "\n\nThe BOK Team.";

        emailMQProducer.send(emailMessage);
    }

}
