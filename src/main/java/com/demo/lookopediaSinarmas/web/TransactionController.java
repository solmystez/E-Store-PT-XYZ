package com.demo.lookopediaSinarmas.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.lookopediaSinarmas.domain.Orders;
import com.demo.lookopediaSinarmas.domain.Transaction;
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
	
//	@PostMapping("/transactionSuccess/{order_identifier}/{user_id}")
//	public ResponseEntity<?> processItem(@Valid @RequestBody Orders order,
//			BindingResult result, @PathVariable String order_identifier, @PathVariable Long user_id) {
//		
//		
//		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
//		if(errorMap != null) return errorMap;
//		
//		Transaction order1 = transactionService.
//				
//		return new ResponseEntity<Transaction>(order1, HttpStatus.CREATED);
//	}
}
