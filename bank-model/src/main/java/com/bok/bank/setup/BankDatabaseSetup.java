package com.bok.bank.setup;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAutoConfiguration
@EnableScheduling
@EnableJpaRepositories("com.bok.bank.repository")
public class BankDatabaseSetup {

}
