package com.mindhub.homebanking2.controllers;

import com.mindhub.homebanking2.Models.*;
import com.mindhub.homebanking2.Services.AccountService;
import com.mindhub.homebanking2.Services.ClientService;
import com.mindhub.homebanking2.Services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import static com.mindhub.homebanking2.Models.TransactionType.CREDIT;
import static com.mindhub.homebanking2.Models.TransactionType.DEBIT;

@RestController
@RequestMapping("/api")
public class TransactionController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private TransactionService transactionService;


	@Transactional
	@PostMapping("/clients/current/transactions")
	public ResponseEntity<Object> newTransaction(@RequestParam Double amount, @RequestParam String description, @RequestParam String accountOrigin, @RequestParam String accountDestiny, Authentication authentication) {

		Client newClientAuthentication = clientService.findClientByEmail(authentication.getName());
		Account newAccountOrigin = accountService.findByNumber(accountOrigin);
		Account newAccountDestiny = accountService.findByNumber(accountDestiny);

		if (newClientAuthentication == null) {
			return new ResponseEntity<>("The Client doesn't exist", HttpStatus.FORBIDDEN);
		}

		if (amount <= 0 ){
			return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
		}
		if (description.isEmpty() || accountOrigin.isEmpty() || accountDestiny.isEmpty()) {
			return new ResponseEntity<>("Same accounts", HttpStatus.FORBIDDEN);
		}
		if (accountOrigin.equals(accountDestiny)) {
			return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
		}
		if (newAccountOrigin == null){
			return new ResponseEntity<>("Origin account doesn't exist", HttpStatus.FORBIDDEN);
		}
		if (newAccountDestiny == null){
			return new ResponseEntity<>("Destiny account doesn't exist", HttpStatus.FORBIDDEN);
		}
		if(accountService.findByNumber(accountOrigin).getBalance() < amount){
			return new ResponseEntity<>("Not enough money", HttpStatus.FORBIDDEN);
		}

		Transaction debitTransaction = new Transaction(DEBIT, - amount, " Go to " + accountDestiny, LocalDateTime.now(), newAccountOrigin);
		Transaction creditTransaction = new Transaction(CREDIT, amount, " From " + accountOrigin, LocalDateTime.now(), newAccountDestiny);
		transactionService.saveTransaction(debitTransaction);
		transactionService.saveTransaction(creditTransaction);

		newAccountOrigin.setBalance(newAccountOrigin.getBalance() - amount);
		newAccountDestiny.setBalance(newAccountDestiny.getBalance() + amount);

		accountService.saveAccount(newAccountOrigin);
		accountService.saveAccount(newAccountDestiny);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}}

