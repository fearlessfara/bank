package com.bok.bank.service;

import com.bok.bank.dto.AccountInfoDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public interface AccountController {

    @GetMapping("/profileInfo")
    AccountInfoDTO profileInfo(@RequestParam("accountId") Long accountId);

}
