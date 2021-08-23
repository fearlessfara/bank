package com.bok.bank;

import com.bok.bank.integration.dto.AuthorizationRequestDTO;
import com.bok.bank.integration.dto.AuthorizationResponseDTO;
import com.bok.bank.integration.service.AccountController;
import com.bok.bank.integration.service.TransactionController;
import com.bok.bank.integration.util.Money;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import static com.bok.bank.ModelTestUtil.EUR;
import static com.bok.bank.ModelTestUtil.USD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
@PropertySource(ignoreResourceNotFound = true, value = "classpath:resources.application-test.yml")
public class TransactionControllerTest {

        @Autowired
        ModelTestUtil modelTestUtil;

        @Autowired
        AccountController accountController;

        @Autowired
        TransactionController transactionController;

        @BeforeEach
        public void init() {
            modelTestUtil.clearAll();
        }


        @Test
    public void checkPaymentAmountTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user);
        AuthorizationResponseDTO checkPaymentAmount = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new Money(bankAccount.getCurrency(), BigDecimal.TEN), "MARKET"));
        assertEquals(checkPaymentAmount.reason, "");
        assertTrue(checkPaymentAmount.authorized);
        //TODO: add other check
    }

    @Test
    public void checkPaymentAmountFailTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        AuthorizationResponseDTO checkPaymentAmount = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new Money(EUR, BigDecimal.valueOf(50)), "MARKET"));
        assertEquals(checkPaymentAmount.reason, "User not have a bank account or is bank account not is active");
        assertFalse(checkPaymentAmount.authorized);
    }

    @Test
    public void checkPaymentAmountAnotherCurrencyTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        AuthorizationResponseDTO checkPaymentAmount = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new Money(USD, BigDecimal.valueOf(121)), "MARKET"));
        assertEquals(checkPaymentAmount.reason, "");
        assertTrue(checkPaymentAmount.authorized);
    }

    @Test
    public void checkPaymentAmountGreaterWithAnotherCurrencyTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        AuthorizationResponseDTO checkPaymentAmount = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new Money(USD, BigDecimal.valueOf(123)), "MARKET"));
        assertEquals(checkPaymentAmount.reason, "Amount not available");
        assertFalse(checkPaymentAmount.authorized);
    }

    @Test
    public void checkPaymentAmountWithNotCommonCurrencyTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user, Currency.getInstance("EUR"));
        AuthorizationResponseDTO checkPaymentAmount = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), UUID.randomUUID(), new Money(Currency.getInstance("AMD"), BigDecimal.valueOf(64156)), "MARKET"));
        assertEquals(checkPaymentAmount.reason, "Amount not available");
        assertFalse(checkPaymentAmount.authorized);
    }
}
