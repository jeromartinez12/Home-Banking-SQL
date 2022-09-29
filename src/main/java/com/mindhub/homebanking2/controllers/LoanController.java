package com.mindhub.homebanking2.controllers;

import com.mindhub.homebanking2.Models.*;
import com.mindhub.homebanking2.Services.*;
import com.mindhub.homebanking2.dtos.LoanApplicationDTO;
import com.mindhub.homebanking2.dtos.LoanDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking2.Models.TransactionType.CREDIT;

@RequestMapping("/api")
@RestController
public class LoanController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private LoanService loanService;

	@Autowired
	private ClientLoanService clientLoanService;

	@Transactional
	@PostMapping("/clients/current/loans")
	public ResponseEntity<String> addLoan(@RequestBody LoanApplicationDTO loanApplicationDTO , Authentication authentication) {

		Client currentClient = clientService.findClientByEmail(authentication.getName());

		Account destinyAccount = accountService.findByNumber(loanApplicationDTO.getDestinyAccount());

		Loan loanId = loanService.getLoanById(loanApplicationDTO.getId());

		if(loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayments() < 1 || loanApplicationDTO.getDestinyAccount().isEmpty()){
			return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
		}

		if (currentClient.getClientLoans().stream().anyMatch(clientLoan -> clientLoan.getLoan() == loanId)) {
			return new ResponseEntity<>("You already have an active "+ loanId.getName(), HttpStatus.FORBIDDEN);
		}

		if(destinyAccount == null){
			return new ResponseEntity<>("account doesn't exist", HttpStatus.FORBIDDEN);
		}

		if(loanApplicationDTO.getAmount() > loanId.getMaxAmount()){
			return new ResponseEntity<>("Amount limit exceeded", HttpStatus.FORBIDDEN);
		}

		if(!loanId.getPayments().contains(loanApplicationDTO.getPayments())){
			return new ResponseEntity<>("Payments doesn't allowed", HttpStatus.FORBIDDEN);
		}

		if(accountService.findByNumber(loanApplicationDTO.getDestinyAccount()) == null){
			return new ResponseEntity<>("destiny account doesn't exist", HttpStatus.FORBIDDEN);
		}

		if(!currentClient.getAccounts().contains(accountService.findByNumber(destinyAccount.getNumber()))){
			return new ResponseEntity<>("Destiny account doesn't match with client user", HttpStatus.FORBIDDEN);
		}

		Double interest = (loanApplicationDTO.getAmount() * (loanId.getInterest()/100)) + loanApplicationDTO.getAmount();

		Transaction creditTransaction = new Transaction(CREDIT,loanApplicationDTO.getAmount(),loanId.getName()+ " loan approved", LocalDateTime.now(),destinyAccount);

		transactionService.saveTransaction(creditTransaction);

		destinyAccount.setBalance(destinyAccount.getBalance() + loanApplicationDTO.getAmount());

		ClientLoan loanPetition = new ClientLoan(interest, loanApplicationDTO.getPayments(),currentClient,loanId);
		clientLoanService.saveLoan(loanPetition);

		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	@GetMapping("/loans")
	public List<LoanDTO> getLoanDTO() {
		return loanService.getAllLoans().stream().map(LoanDTO::new).collect(Collectors.toList());
	}
}
