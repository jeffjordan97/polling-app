package com.dizplai.polling.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = "com.dizplai.polling.service")
@EnableCaching
public class PollingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PollingServiceApplication.class, args);
	}

}
