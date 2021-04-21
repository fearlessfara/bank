package com.bok.bank.service;

import com.bok.bank.dto.BankAccountInfoDTO;
import com.bok.bank.dto.CheckPaymentAmountRequestDTO;
import com.bok.bank.dto.CheckPaymentAmountResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bankAccount")
public interface BankAccountController {

    @PostMapping("/checkPaymentAmount/{accountId}")
    CheckPaymentAmountResponseDTO checkPaymentAmount(@PathVariable("accountId") Long accountId, @RequestBody CheckPaymentAmountRequestDTO checkPaymentAmountRequestDTO);

    @GetMapping("/bankAccountInfo/{accountId}")
    BankAccountInfoDTO bankAccountInfo(@PathVariable("accountId") Long accountId);

}
