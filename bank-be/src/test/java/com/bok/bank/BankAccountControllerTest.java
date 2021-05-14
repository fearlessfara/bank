package com.bok.bank;

import com.bok.bank.dto.BankAccountInfoDTO;
import com.bok.bank.dto.CheckPaymentAmountRequestDTO;
import com.bok.bank.dto.CheckPaymentAmountResponseDTO;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.User;
import com.bok.bank.service.BankAccountController;
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

import static com.bok.bank.ModelTestUtil.faker;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
        CheckPaymentAmountResponseDTO checkPaymentAmount = bankAccountController.checkPaymentAmount(user.getId(), new CheckPaymentAmountRequestDTO(bankAccount.getCurrency(), BigDecimal.valueOf(50)));
        assertEquals(checkPaymentAmount.reason, "");
        assertTrue(checkPaymentAmount.available);
    }

    @Test
    public void checkPaymentAmountFailTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        CheckPaymentAmountResponseDTO checkPaymentAmount = bankAccountController.checkPaymentAmount(user.getId(), new CheckPaymentAmountRequestDTO(Currency.getInstance("EUR"), BigDecimal.valueOf(50)));
        assertEquals(checkPaymentAmount.reason, "User not have a bank account or is bank account not is active");
        assertFalse(checkPaymentAmount.available);
    }

    @Test
    public void checkPaymentAmountAnotherCurrencyTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        CheckPaymentAmountResponseDTO checkPaymentAmount = bankAccountController.checkPaymentAmount(user.getId(), new CheckPaymentAmountRequestDTO(Currency.getInstance("USD"), BigDecimal.valueOf(121)));
        assertEquals(checkPaymentAmount.reason, "");
        assertTrue(checkPaymentAmount.available);
    }

    @Test
    public void checkPaymentAmountGreaterWithAnotherCurrencyTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        CheckPaymentAmountResponseDTO checkPaymentAmount = bankAccountController.checkPaymentAmount(user.getId(), new CheckPaymentAmountRequestDTO(Currency.getInstance("USD"), BigDecimal.valueOf(123)));
        assertEquals(checkPaymentAmount.reason, "Amount not available");
        assertFalse(checkPaymentAmount.available);
    }

    @Test
    public void checkPaymentAmountWithNotCommonCurrencyTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        CheckPaymentAmountResponseDTO checkPaymentAmount = bankAccountController.checkPaymentAmount(user.getId(), new CheckPaymentAmountRequestDTO(Currency.getInstance("AMD"), BigDecimal.valueOf(64156)));
        assertEquals(checkPaymentAmount.reason, "Amount not available");
        assertFalse(checkPaymentAmount.available);
    }

}
