package com.bok.bank.service;

import com.bok.bank.dto.AccountInfoDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public interface AccountController {

    @GetMapping("/profileInfo/{accountId}")
    AccountInfoDTO profileInfo(@PathVariable("accountId") Long accountId);

}
