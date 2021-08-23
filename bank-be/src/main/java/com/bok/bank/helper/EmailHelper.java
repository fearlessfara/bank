package com.bok.bank.helper;

import com.bok.bank.messaging.EmailMQProducer;
import com.bok.bank.model.Account;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.Card;
import com.bok.bank.model.ConfirmationEmailHistory;
import com.bok.parent.integration.message.EmailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailHelper {

    @Autowired
    EmailMQProducer emailMQProducer;

    @Autowired
    ConfirmationEmailHelper confirmationEmailHelper;

    @Value("${server.base-url}")
    String baseUrl;


    public void sendBankAccountConfirmationEmail(Account account, BankAccount bankAccount) {
        ConfirmationEmailHistory confirmationEmailHistory = confirmationEmailHelper.saveConfirmationEmail(account, bankAccount.getId(), ConfirmationEmailHistory.ResourceType.BANK_ACCOUNT);
        log.info("email: {}", account.getEmail());
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.to = account.getEmail();
        emailMessage.subject = "BOK - Bank Account activation";
        emailMessage.body = "Dear " + account.getName() + ",\nClick on the following link to complete creation of the bank account named " + bankAccount.getName() + "\n" +
                baseUrl + "/bankAccount/verify?accountId=" + account.getId() + "&verificationToken=" + confirmationEmailHistory.getConfirmationToken() +
                "\n\nThe BOK Team.";

        emailMQProducer.send(emailMessage);
    }

    //TODO create the grpc listener for this method
    public void sendCardConfirmationEmail(Account account, Card card) {
        ConfirmationEmailHistory confirmationEmailHistory = confirmationEmailHelper.saveConfirmationEmail(account, card.getId(), ConfirmationEmailHistory.ResourceType.CARD);
        log.info("email: {}", account.getEmail());
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.to = account.getEmail();
        emailMessage.subject = "BOK - Card activation";
        emailMessage.body = "Dear " + account.getName() + ",\nClick on the following link to complete creation of the card named " + card.getName() + "\n" +
                "https://api.bok.faraone.ovh/cardConfirm/" + account.getId() + "/" + confirmationEmailHistory.getConfirmationToken() +
                "\n\nThe BOK Team.";

        emailMQProducer.send(emailMessage);
    }


}
