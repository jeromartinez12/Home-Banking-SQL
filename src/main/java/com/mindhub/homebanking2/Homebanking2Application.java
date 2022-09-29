package com.mindhub.homebanking2;
import com.mindhub.homebanking2.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Homebanking2Application {

	public static void main(String[] args) {
		SpringApplication.run(Homebanking2Application.class, args);
	}
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientService clientService, AccountService accountService, TransactionService transactionService, ClientLoanService clientLoanService, LoanService loanService, CardService cardService) {
		return (args) -> { };
	}

}

