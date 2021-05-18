package com.bok.bank.integration.service;

import com.bok.bank.integration.dto.AccountInfoDTO;
import com.bok.bank.integration.dto.BankCheckRequestDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public interface AccountController {

    @GetMapping("/profileInfo")
    AccountInfoDTO profileInfo(@RequestParam("accountId") Long accountId);

    @PostMapping("/checkCreation")
    Boolean checkCreation(@RequestBody BankCheckRequestDTO bankCheckRequestDTO);

}
