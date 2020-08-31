package com.demo.lookopediaSinarmas.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.lookopediaSinarmas.services.StatusService;
import com.demo.lookopediaSinarmas.services.TransactionService;
import com.demo.lookopediaSinarmas.services.otherService.MapValidationErrorService;

@CrossOrigin
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private StatusService statusService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
}
