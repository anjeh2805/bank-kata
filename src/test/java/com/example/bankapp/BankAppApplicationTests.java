package com.example.bankapp;

import com.example.bankapp.controller.AccountController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
class BankAppApplicationTests {

	@Autowired
	private AccountController accountController;

	@Test
	void contextLoads() throws Exception{
		assertThat(accountController).isNotNull();
	}

}
