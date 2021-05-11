package com.bok.bank;

import com.bok.bank.dto.AccountInfoDTO;
import com.bok.bank.model.Company;
import com.bok.bank.model.User;
import com.bok.bank.service.AccountController;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class AccountControllerTest {

    @Autowired
    ModelTestUtil modelTestUtil;

    @Autowired
    AccountController accountController;

    @Before
    public void init(){
        modelTestUtil.clearAll();
    }

    @Test
    public void profileInfoUserTest(){
        User user = modelTestUtil.createAndSaveUser(17L);
        log.info(user.toString());

        AccountInfoDTO accountInfoDTO = accountController.profileInfo(17L);
        assertEquals(accountInfoDTO.email, user.getEmail());
        assertEquals(accountInfoDTO.icc, user.getIcc());
        assertEquals(accountInfoDTO.mobile, user.getMobile());
        assertEquals(accountInfoDTO.status, user.getStatus().name());
        assertEquals(accountInfoDTO.type, user.getType().name());
        assertEquals(accountInfoDTO.fullName, user.getName() + " " + user.getMiddleName() + " " + user.getSurname());
    }
    @Test
    public void profileInfoCompanyTest(){
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
    public void profileInfoWithIdNotPresentTest(){
        assertThrows(IllegalArgumentException.class, () -> accountController.profileInfo(17L));
    }
    @Test
    public void profileInfoWithIdNotPassedTest(){
        assertThrows(NullPointerException.class, () -> accountController.profileInfo(null));
    }

}
