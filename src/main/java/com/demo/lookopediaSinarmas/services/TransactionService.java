package com.demo.lookopediaSinarmas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.repositories.TransactionRepository;

@Service
public class TransactionService {
	
	@Autowired
	TransactionRepository transactionRepository;
	
	//generate random ident for invoice
	//check status here
	
}
