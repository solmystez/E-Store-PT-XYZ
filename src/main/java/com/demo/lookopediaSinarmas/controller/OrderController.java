package com.demo.lookopediaSinarmas.controller;

import java.security.Principal;
import java.util.List;

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

import com.demo.lookopediaSinarmas.entity.Cart;
import com.demo.lookopediaSinarmas.entity.Orders;
import com.demo.lookopediaSinarmas.entity.Product;
import com.demo.lookopediaSinarmas.services.MerchantService;
import com.demo.lookopediaSinarmas.services.OrderService;
import com.demo.lookopediaSinarmas.services.otherService.MapValidationErrorService;

@CrossOrigin
@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private MerchantService merchantService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	
	@GetMapping("/loadAllHistory/{user_id}")
	public Iterable<Orders> loadAllHistory(@PathVariable Long user_id){
		return orderService.loadAllOrderByUserIdForHistory(user_id);	
	}
	
//	@GetMapping("/loadOrderDetails/{order_identifier}")
//	public ResponseEntity<?> loadProduct(@PathVariable String order_identifier){
//		
//		Orders orders = orderService.findDetailOrder(order_identifier);
//
//		return new ResponseEntity<Orders>(orders, HttpStatus.OK);
//	}
	
	@PostMapping("/checkout/{user_id}")
	public ResponseEntity<?> processItem(@Valid @RequestBody Orders orders,
			BindingResult result, @PathVariable Long user_id, Principal principal) {
		//req body = status = "paid";
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
		if(errorMap != null) return errorMap;
		
		Iterable<Orders> order1 = orderService.processItem(orders, user_id, principal.getName());
				
		return new ResponseEntity<Iterable<Orders>>(order1, HttpStatus.CREATED);
	}
	
	@PostMapping("/accOrRejectOrder/{order_id}")
	public ResponseEntity<?> accOrRejectOrderMerchant(@Valid @RequestBody Orders order,
			BindingResult result, @PathVariable Long order_id){

			ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
			if(mapError != null) return mapError;
			
			Orders orders1 = merchantService.accOrRejectProductOrder(order, order_id);
			return new ResponseEntity<Orders>(orders1, HttpStatus.CREATED);
	}
	
	//post finish button{set status to finish}
	@PostMapping("/finishOrder/{order_id}")
	public ResponseEntity<?> userFinishOrder(@Valid @RequestBody Orders order,
			BindingResult result, @PathVariable Long order_id){

			ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
			if(mapError != null) return mapError;
			
			Orders orders1 = merchantService.userFinishOrder(order, order_id);
			return new ResponseEntity<Orders>(orders1, HttpStatus.CREATED);
	}
	
	@GetMapping("/getTotal/{user_id}")
	public List<Orders> getTotalPriceAndItem(@PathVariable Long user_id){
		return orderService.countAllTotalForCart(user_id);
	}
	
//	@PostMapping("/finishTransaction")
//	public ResponseEntity<?> processItem(@Valid @RequestBody Orders orders,
//			BindingResult result, @PathVariable Long user_id, Principal principal) {
//		//req body = status = "paid";
//		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
//		if(errorMap != null) return errorMap;
//		
//		Iterable<Orders> order1 = orderService.processItem(orders, user_id, principal.getName());
//				
//		return new ResponseEntity<Iterable<Orders>>(order1, HttpStatus.CREATED);
//	}
}
