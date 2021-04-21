package com.bok.bank.helper;

import com.bok.bank.dto.AccountInfoDTO;
import com.bok.bank.model.Account;
import com.bok.bank.model.Company;
import com.bok.bank.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountHelper {
    @Autowired
    AccountRepository accountRepository;

    public AccountInfoDTO getAccountInfo(Long accountId){
        AccountRepository.Projection.AccountInfo accountInfo = accountRepository.findAccountInfoByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for accountId "+ accountId));
        String fullName;
        if(accountInfo.getType().equals(Account.Type.INDIVIDUAL_USER)) {
               fullName = accountInfo.getName() + " " + accountInfo.getMiddleName() + " " + accountInfo.getSurname();
        } else{
            fullName = accountInfo.getName();
        }
        return new AccountInfoDTO(accountInfo.getUsername(), accountInfo.getEmail(), accountInfo.getIcc(), accountInfo.getMobile(), accountInfo.getStatus().name(), accountInfo.getType().name(), fullName);
    }
}
