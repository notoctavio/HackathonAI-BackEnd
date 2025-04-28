package com.ProMatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.ProMatch")
@EnableJpaRepositories(basePackages = "com.ProMatch.repository")
public class ProMatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProMatchApplication.class, args);
    }
} 