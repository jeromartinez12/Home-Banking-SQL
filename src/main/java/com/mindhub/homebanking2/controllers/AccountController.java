package com.mindhub.homebanking2.controllers;

import com.mindhub.homebanking2.Models.Account;
import com.mindhub.homebanking2.Models.Client;
import com.mindhub.homebanking2.Services.AccountService;
import com.mindhub.homebanking2.Services.ClientService;
import com.mindhub.homebanking2.dtos.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    //servlet
    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAllAccounts().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    //servlet: son clases Java diseñadas para responder a solicitudes HTTP.
    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return new AccountDTO(accountService.getAccountById(id));
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> newAccount (
            Authentication authentication) {

        String accountNumber = "VIN - " + getRandomNumber(1, 99999999);

        Client client = clientService.findClientByEmail(authentication.getName());

        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("You already have three accounts", HttpStatus.FORBIDDEN);
        }

        accountService.saveAccount(new Account(accountNumber, LocalDateTime.now(), 0.0, client));

        return new ResponseEntity<>("Your new account was created successfully",HttpStatus.CREATED);
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

