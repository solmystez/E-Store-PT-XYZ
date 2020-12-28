package com.demo.lookopediaSinarmas.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.lookopediaSinarmas.entity.Cart;
import com.demo.lookopediaSinarmas.entity.Orders;
import com.demo.lookopediaSinarmas.services.CartService;
import com.demo.lookopediaSinarmas.services.otherService.MapValidationErrorService;

@CrossOrigin
@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	//addProduct to invoice With ProductId
	@PostMapping("/addProduct/{product_id}/{user_id}")
	public ResponseEntity<?> addProductToCartOrAddQty(
			@PathVariable Long product_id, @PathVariable Long user_id,
			Principal principal){
//		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
//		if(mapError != null) return mapError;
		
		Orders orders1 = cartService.addProductToCartOrAddQty(product_id, user_id, principal.getName());
		return new ResponseEntity<Orders>(orders1, HttpStatus.CREATED);
	}
	
	@PostMapping("/subProduct/{product_id}/{user_id}")
	public ResponseEntity<?> subProductFromCart(
			@PathVariable Long product_id, @PathVariable Long user_id, 
			Principal principal){
				
		Orders orders1 = cartService.removeProductFromCartOrSubQty(product_id, user_id, principal.getName());
		return new ResponseEntity<Orders>(orders1, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/selectCourier")
	public ResponseEntity<?> addCourierPrice(@Valid @RequestBody Orders order, BindingResult result,
			 							 Principal principal){
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Orders order1 = cartService.selectCourier(order, principal.getName());
		return new ResponseEntity<Orders>(order1, HttpStatus.OK);
	}
	
	@GetMapping("/getTotalItem/{user_id}")
	public int getTotalItem(@PathVariable Long user_id){
		return cartService.returnTotalItem(user_id);
	}
	
	@GetMapping("/getTotalPrice/{user_id}")
	public int getTotalPrice(@PathVariable Long user_id){
		return cartService.returnTotalPrice(user_id);
	}
	
	//delete product di cart, bukan delete product
	//delete by product_id di cart by username dan status not paid
	@DeleteMapping("/deleteProductFromCart/{product_id}")
	public ResponseEntity<?> deleteProductFromCart(Principal principal, @PathVariable Long product_id) {
		List<Cart> cart1 = cartService.removeProductFromCart(product_id, principal.getName());
		
		return new ResponseEntity<List<Cart>>(cart1, HttpStatus.OK);
	}

}
