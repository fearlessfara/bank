package com.bok.bank;

import com.bok.bank.setup.BankDatabaseSetup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableEurekaClient
@EnableScheduling
@EnableJpaRepositories("com.bok.bank.repository")
public class BankApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
    }

}
