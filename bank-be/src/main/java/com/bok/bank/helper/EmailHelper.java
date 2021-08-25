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

    public void sendCardConfirmationEmail(Account account, Card card) {
        log.info("email: {}", account.getEmail());
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.to = account.getEmail();
        emailMessage.subject = "BOK - Card activation";
        emailMessage.body = "Dear " + account.getName() +
                ",\nThis is the PIN of your new card called: " + card.getName() + ".\n\n"
                + card.getPIN()
                + "\n\nIt can be used to active your card. \n\nPlease insert the PIN to active the new card \n https://bok.faraone.ovh/"
                + "\n\nThe BOK Team.";
        emailMQProducer.send(emailMessage);
    }
    public String sendPINEmail(Account account, Card card) {
        log.info("email: {}", account.getEmail());
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.to = account.getEmail();
        emailMessage.subject = "BOK - Card PIN";
        emailMessage.body = "Dear " + account.getName() + ",\nThe PIN of the card: " + card.getName() + " is:\n\n"
                + card.getPIN()
                + "\n\nThe BOK Team.";
        emailMQProducer.send(emailMessage);
        return "Please check your email, the PIN has been send";
    }
}
