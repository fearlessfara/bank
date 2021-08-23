package com.bok.bank;

import com.bok.bank.helper.AccountHelper;
import com.bok.bank.integration.dto.AccountInfoDTO;
import com.bok.bank.integration.service.AccountController;
import com.bok.bank.model.Account;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.Company;
import com.bok.bank.model.User;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.util.Money;
import com.bok.parent.integration.message.AccountCreationMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import static com.bok.bank.model.Account.Status.ACTIVE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
@PropertySource(ignoreResourceNotFound = true, value = "classpath:resources.application-test.yml")
public class AccountControllerTest {

    @Autowired
    ModelTestUtil modelTestUtil;

    @Autowired
    AccountController accountController;

    @Autowired
    AccountHelper accountHelper;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @BeforeEach
    public void init() {
        modelTestUtil.clearAll();
    }

    @Test
    public void profileInfoUserTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        log.info(user.toString());

        AccountInfoDTO accountInfoDTO = accountController.profileInfo(17L);
        assertEquals(accountInfoDTO.email, user.getEmail());
        assertEquals(accountInfoDTO.icc, user.getIcc());
        assertEquals(accountInfoDTO.mobile, user.getMobile());
        assertEquals(accountInfoDTO.status, user.getStatus().name());
        assertEquals(accountInfoDTO.type, user.getType().name());
        assertEquals(accountInfoDTO.fullName, (user.getName() + " " + user.getMiddleName() + " " + user.getSurname()).trim().replaceAll(" +", " "));
    }

    @Test
    public void profileInfoCompanyTest() {
        Company company = modelTestUtil.createAndSaveCompany(17L);
        log.info(company.toString());

        AccountInfoDTO accountInfoDTO = accountController.profileInfo(17L);
        assertEquals(accountInfoDTO.email, company.getEmail());
        assertEquals(accountInfoDTO.icc, company.getIcc());
        assertEquals(accountInfoDTO.mobile, company.getMobile());
        assertEquals(accountInfoDTO.status, company.getStatus().name());
        assertEquals(accountInfoDTO.type, company.getType().name());
        assertEquals(accountInfoDTO.fullName, company.getName());
    }

    @Test
    public void profileInfoWithIdNotPresentTest() {
        assertThrows(IllegalArgumentException.class, () -> accountController.profileInfo(17L));
    }

    @Test
    public void profileInfoWithIdNotPassedTest() {
        assertThrows(NullPointerException.class, () -> accountController.profileInfo(null));
    }

    @Test
    public void userCreationTest() {
        AccountCreationMessage accountCreationMessage = new AccountCreationMessage("Domenico", "", "Fasano", "mico@gmail.com", new Date(10212541), "Fasano", "Italia", false, "FSNDMC99C13D508Y", "", "+39", "3926772950", "23", "via le mani dal naso", "Locorotondo", "BA", "Italy", "70010", 123L, User.Gender.M.name());
        accountHelper.createAccount(accountCreationMessage);
        AccountInfoDTO accountInfo = accountHelper.getAccountInfo(123L);
        Assertions.assertEquals(accountInfo.email, accountCreationMessage.email);
        Assertions.assertEquals(accountInfo.fullName, accountCreationMessage.name + " " + accountCreationMessage.surname);
        Assertions.assertEquals(accountInfo.mobile, accountCreationMessage.mobile);
        Assertions.assertEquals(accountInfo.type, Account.Type.INDIVIDUAL_USER.name());
        Assertions.assertEquals(accountInfo.status, ACTIVE.name());
        BankAccount bankAccount = bankAccountRepository.findByAccountId(123L).get();
        Assertions.assertNotNull(bankAccount);
        Assertions.assertEquals(bankAccount.getAvailableAmount(), new Money(BigDecimal.valueOf(100000).setScale(2, RoundingMode.FLOOR), bankAccount.getCurrency()));
        Assertions.assertEquals(bankAccount.getBlockedAmount(), new Money(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), bankAccount.getCurrency()));
    }

}
