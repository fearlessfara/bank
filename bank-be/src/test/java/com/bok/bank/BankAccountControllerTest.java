package com.bok.bank;

import com.bok.bank.integration.dto.BankAccountInfoDTO;
import com.bok.bank.integration.dto.AuthorizationRequestDTO;
import com.bok.bank.integration.dto.AuthorizationResponseDTO;
import com.bok.bank.integration.service.BankAccountController;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class BankAccountControllerTest {

    @Autowired
    BankAccountController bankAccountController;

    @Autowired
    ModelTestUtil modelTestUtil;

    @Before
    public void init() {
        modelTestUtil.clearAll();
        modelTestUtil.populateDB();
    }

    @Test
    public void newBankAccountTest() {
//        User user = modelTestUtil.createAndSaveUser(17L);
//        log.info(user.toString());
//        BankAccountDTO bankAccountDTO = new BankAccountDTO(faker.funnyName().name(), faker.lorem().paragraph(), Currency.getInstance(faker.currency().code()));
//        BankAccountInfoDTO bankAccountInfoDTO = bankAccountController.newBankAccount(user.getId(), bankAccountDTO);
//        assertEquals(bankAccountInfoDTO.bankAccountName, bankAccountDTO.name);
//        assertEquals(bankAccountInfoDTO.label, bankAccountDTO.label);
//        assertEquals(bankAccountInfoDTO.currency, bankAccountDTO.currency);
    }

    @Test
    public void bankAccountInfoTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        log.info(user.toString());
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user);
        BankAccountInfoDTO bankAccountInfoDTO = bankAccountController.bankAccountInfo(user.getId());
        assertEquals(bankAccountInfoDTO.bankAccountName, bankAccount.getName());
        assertEquals(bankAccountInfoDTO.email, user.getEmail());
        assertEquals(bankAccountInfoDTO.IBAN, bankAccount.getIBAN());
        assertEquals(bankAccountInfoDTO.label, bankAccount.getLabel());
        assertEquals(bankAccountInfoDTO.currency, bankAccount.getCurrency());
        assertEquals(bankAccountInfoDTO.blockedAmount.setScale(6), bankAccount.getBlockedAmount().getValue().setScale(6));
        assertEquals(bankAccountInfoDTO.availableAmount.setScale(6), bankAccount.getAvailableAmount().getValue().setScale(6));
        assertEquals(bankAccountInfoDTO.status, bankAccount.getStatus().name());
    }

    @Test
    public void checkPaymentAmountTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user);
        AuthorizationResponseDTO checkPaymentAmount = bankAccountController.authorize(user.getId(), new AuthorizationRequestDTO(bankAccount.getCurrency(), BigDecimal.valueOf(50)));
        assertEquals(checkPaymentAmount.reason, "");
        assertTrue(checkPaymentAmount.authorized);
    }

    @Test
    public void checkPaymentAmountFailTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        AuthorizationResponseDTO checkPaymentAmount = bankAccountController.authorize(user.getId(), new AuthorizationRequestDTO(Currency.getInstance("EUR"), BigDecimal.valueOf(50)));
        assertEquals(checkPaymentAmount.reason, "User not have a bank account or is bank account not is active");
        assertFalse(checkPaymentAmount.authorized);
    }

    @Test
    public void checkPaymentAmountAnotherCurrencyTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        AuthorizationResponseDTO checkPaymentAmount = bankAccountController.authorize(user.getId(), new AuthorizationRequestDTO(Currency.getInstance("USD"), BigDecimal.valueOf(121)));
        assertEquals(checkPaymentAmount.reason, "");
        assertTrue(checkPaymentAmount.authorized);
    }

    @Test
    public void checkPaymentAmountGreaterWithAnotherCurrencyTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        AuthorizationResponseDTO checkPaymentAmount = bankAccountController.authorize(user.getId(), new AuthorizationRequestDTO(Currency.getInstance("USD"), BigDecimal.valueOf(123)));
        assertEquals(checkPaymentAmount.reason, "Amount not available");
        assertFalse(checkPaymentAmount.authorized);
    }

    @Test
    public void checkPaymentAmountWithNotCommonCurrencyTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        AuthorizationResponseDTO checkPaymentAmount = bankAccountController.authorize(user.getId(), new AuthorizationRequestDTO(Currency.getInstance("AMD"), BigDecimal.valueOf(64156)));
        assertEquals(checkPaymentAmount.reason, "Amount not available");
        assertFalse(checkPaymentAmount.authorized);
    }

}
