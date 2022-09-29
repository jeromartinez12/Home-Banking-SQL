package com.mindhub.homebanking2.Services.Implements;

import com.mindhub.homebanking2.Models.Transaction;
import com.mindhub.homebanking2.Repositories.TransactionRepository;
import com.mindhub.homebanking2.Services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImplement implements TransactionService {

	@Autowired
	TransactionRepository transactionRepository;

	@Override
	public void saveTransaction(Transaction transaction){
		transactionRepository.save(transaction);
	}
}
