package com.bok.bank.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface MainController {

    @GetMapping("/db-populator")
    String populateDB();
    @GetMapping("/db-populator-card")
    String populateDBCard();
    @GetMapping("/db-populator-bankAccount")
    String populateDBBankAccount();

    @PostMapping("/clean-database")
    String cleanDB();


}
