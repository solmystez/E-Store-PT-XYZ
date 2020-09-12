package com.demo.lookopediaSinarmas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.repositories.TransactionRepository;

@Service
public class TransactionService {
	
	@Autowired
	TransactionRepository transactionRepository;
	
	//transactionService ini ngatur buat bikin 'paid' 
	
	//if transaction status set == paid
	//-. --stock product in merchant <= terjadi pas bkin orderNumber(orderService), bkn disini
	//-. send totalPrice to merchant
	
	
	//1. find lg cart yg mana, 
}
