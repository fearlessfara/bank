package com.bok.bank.integration.service;

import com.bok.bank.integration.dto.AuthorizationRequestDTO;
import com.bok.bank.integration.dto.AuthorizationResponseDTO;
import com.bok.bank.integration.dto.BankAccountDTO;
import com.bok.bank.integration.dto.BankAccountInfoDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bankAccount")
public interface BankAccountController {

    @PostMapping("/authorize")
    AuthorizationResponseDTO authorize(@RequestParam("accountId") Long accountId, @RequestBody AuthorizationRequestDTO authorizationRequestDTO);

    @GetMapping("/bankAccountInfo")
    BankAccountInfoDTO bankAccountInfo(@RequestParam("accountId") Long accountId);

    @PostMapping("/create")
    String createBankAccount(@RequestParam("accountId") Long accountId, @RequestBody BankAccountDTO bankAccountDTO);

    @GetMapping("/verify")
    BankAccountInfoDTO verify(@RequestParam("accountId") Long accountId, String confirmationToken);

}
