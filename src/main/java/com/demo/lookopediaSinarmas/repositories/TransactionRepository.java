package com.demo.lookopediaSinarmas.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.domain.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long>{
	
}