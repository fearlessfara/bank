package com.bok.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(BankDatabaseSetup.class)
public class BankApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);

    }
}
