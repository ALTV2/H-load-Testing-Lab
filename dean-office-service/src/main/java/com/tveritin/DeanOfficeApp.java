package com.tveritin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DeanOfficeApp {
    public static void main(String[] args) {
        SpringApplication.run(DeanOfficeApp.class, args);
    }
}