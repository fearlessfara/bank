package com.bok.bank;

import com.bok.bank.helper.AccountHelper;
import com.bok.bank.integration.dto.AccountInfoDTO;
import com.bok.bank.integration.dto.AuthorizationRequestDTO;
import com.bok.bank.integration.dto.AuthorizationResponseDTO;
import com.bok.bank.integration.service.AccountController;
import com.bok.bank.integration.service.TransactionController;
import com.bok.bank.messaging.AccountConsumer;
import com.bok.bank.model.Account;
import com.bok.bank.model.BankAccount;
import com.bok.bank.model.Card;
import com.bok.bank.model.Company;
import com.bok.bank.model.User;
import com.bok.bank.repository.AccountRepository;
import com.bok.bank.repository.BankAccountRepository;
import com.bok.bank.util.Money;
import com.bok.parent.integration.message.AccountClosureMessage;
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
import java.util.Optional;

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
    TransactionController transactionController;

    @Autowired
    AccountConsumer accountConsumer;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    AccountRepository accountRepository;

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

    @Test
    public void companyCreationTest() {
        AccountCreationMessage accountCreationMessage = new AccountCreationMessage("Domenico", null, null, "mico@gmail.com", null, null, null, true, null, "123456789098765", "+39", "3926772950", "23", "via le mani dal naso", "Locorotondo", "BA", "Italy", "70010", 123L, null);
        accountHelper.createAccount(accountCreationMessage);
        AccountInfoDTO accountInfo = accountHelper.getAccountInfo(123L);
        Assertions.assertEquals(accountInfo.email, accountCreationMessage.email);
        Assertions.assertEquals(accountInfo.fullName, accountCreationMessage.name);
        Assertions.assertEquals(accountInfo.mobile, accountCreationMessage.mobile);
        Assertions.assertEquals(accountInfo.type, Account.Type.COMPANY.name());
        Assertions.assertEquals(accountInfo.status, ACTIVE.name());
        BankAccount bankAccount = bankAccountRepository.findByAccountId(123L).get();
        Assertions.assertNotNull(bankAccount);
        Assertions.assertEquals(bankAccount.getAvailableAmount(), new Money(BigDecimal.valueOf(100000).setScale(2, RoundingMode.FLOOR), bankAccount.getCurrency()));
        Assertions.assertEquals(bankAccount.getBlockedAmount(), new Money(BigDecimal.valueOf(0).setScale(2, RoundingMode.FLOOR), bankAccount.getCurrency()));
    }

    @Test
    public void deleteUserTest() {
        AccountCreationMessage accountCreationMessage = new AccountCreationMessage("Domenico", "", "Fasano", "mico@gmail.com", new Date(10212541), "Fasano", "Italia", false, "FSNDMC99C13D508Y", "", "+39", "3926772950", "23", "via le mani dal naso", "Locorotondo", "BA", "Italy", "70010", 123L, User.Gender.M.name());
        accountHelper.createAccount(accountCreationMessage);
        accountConsumer.deleteUserListener(new AccountClosureMessage(123L, "AAAA"));
        Optional<Account> account = accountRepository.findById(123L);
        Optional<BankAccount> bankAccount = bankAccountRepository.findByAccountId(123L);
        Assertions.assertFalse(account.isPresent());
        Assertions.assertFalse(bankAccount.isPresent());
    }

    @Test
    public void deleteUserFullTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user);
        Card card = modelTestUtil.createAndSaveActiveCard(user, bankAccount);
        Money moneyToAuthorize = new Money(new BigDecimal(10).setScale(2, RoundingMode.FLOOR), bankAccount.getCurrency());
        AuthorizationResponseDTO checkPaymentAmount = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), null, new com.bok.bank.integration.util.Money(moneyToAuthorize.getCurrency(), moneyToAuthorize.getValue()), "MARKET", card.getToken()));
        bankAccount = bankAccountRepository.findById(bankAccount.getId()).get();
        bankAccount.setBlockedAmount(Money.ZERO);
        bankAccountRepository.saveAndFlush(bankAccount);
        accountConsumer.deleteUserListener(new AccountClosureMessage(17L, "AAAA"));
        Optional<Account> account = accountRepository.findById(17L);
        Optional<BankAccount> bankAccountOpt = bankAccountRepository.findByAccountId(17L);
        Assertions.assertFalse(account.isPresent());
        Assertions.assertFalse(bankAccountOpt.isPresent());
    }

    @Test
    public void deleteUserFullFailBlockedAmountTest() {
        User user = modelTestUtil.createAndSaveUser(17L);
        BankAccount bankAccount = modelTestUtil.createAndSaveBankAccount(user);
        Card card = modelTestUtil.createAndSaveActiveCard(user, bankAccount);
        Money moneyToAuthorize = new Money(new BigDecimal(10).setScale(2, RoundingMode.FLOOR), bankAccount.getCurrency());
        AuthorizationResponseDTO checkPaymentAmount = transactionController.authorize(user.getId(), new AuthorizationRequestDTO(user.getId(), null, new com.bok.bank.integration.util.Money(moneyToAuthorize.getCurrency(), moneyToAuthorize.getValue()), "MARKET", card.getToken()));
        Assertions.assertThrows(IllegalStateException.class, () -> accountConsumer.deleteUserListener(new AccountClosureMessage(17L, "AAAA")));
    }

}
