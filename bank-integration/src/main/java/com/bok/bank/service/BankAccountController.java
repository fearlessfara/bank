package com.bok.bank.service;

import com.bok.bank.dto.BankAccountInfoDTO;
import com.bok.bank.dto.CheckPaymentAmountRequestDTO;
import com.bok.bank.dto.CheckPaymentAmountResponseDTO;
import com.bok.bank.dto.BankAccountDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bankAccount")
public interface BankAccountController {

    @PostMapping("/checkPaymentAmount")
    CheckPaymentAmountResponseDTO checkPaymentAmount(@RequestParam("accountId") Long accountId, @RequestBody CheckPaymentAmountRequestDTO checkPaymentAmountRequestDTO);

    @GetMapping("/bankAccountInfo")
    BankAccountInfoDTO bankAccountInfo(@RequestParam("accountId") Long accountId);

    @PostMapping("/create")
    String createBankAccount(@RequestParam("accountId") Long accountId, BankAccountDTO bankAccountDTO);

    @PostMapping("/verify")
    BankAccountInfoDTO verify(@RequestParam("accountId") Long accountId, String confirmationToken);

}
