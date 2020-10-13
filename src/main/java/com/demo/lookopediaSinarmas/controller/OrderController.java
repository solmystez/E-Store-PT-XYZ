package com.demo.lookopediaSinarmas.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.lookopediaSinarmas.entity.Orders;
import com.demo.lookopediaSinarmas.services.OrderService;
import com.demo.lookopediaSinarmas.services.otherService.MapValidationErrorService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	@GetMapping("/loadAllOrder/{user_id}")
	public Iterable<Orders> loadAllIOrderUser(@PathVariable Long user_id){
		return orderService.loadAllOrderByUserId(user_id);	
	}
	
	@PostMapping("/processItem/{order_identifier}/{user_id}")
	public ResponseEntity<?> processItem(@Valid @RequestBody Orders order,
			BindingResult result, @PathVariable String order_identifier, @PathVariable Long user_id) {
		
		
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
		if(errorMap != null) return errorMap;
		
		Orders order1 = orderService.processItem(order_identifier, user_id);
				
		return new ResponseEntity<Orders>(order1, HttpStatus.CREATED);
	}
	

}
