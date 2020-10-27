package com.example.bankapp;

import org.dozer.DozerBeanMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.logging.Logger;

@SpringBootApplication
public class BankAppApplication {

	public static final Logger LOG = Logger.getLogger(BankAppApplication.class.getName());

	@Bean
	public DozerBeanMapper mapper() {
		final DozerBeanMapper mapper = new DozerBeanMapper();
		return mapper;
	}

	public static void main(String[] args) {
		SpringApplication.run(BankAppApplication.class, args);
	}

}
