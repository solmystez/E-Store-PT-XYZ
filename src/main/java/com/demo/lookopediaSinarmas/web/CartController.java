package com.demo.lookopediaSinarmas.web;

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

import com.demo.lookopediaSinarmas.domain.Cart;
import com.demo.lookopediaSinarmas.domain.Orders;
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
	public ResponseEntity<?> addProductToCartOrAddQty(@Valid @RequestBody Orders invoice, BindingResult result,
			 							@PathVariable Long product_id, @PathVariable Long user_id){
				
		
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Orders invoice1 = cartService.addProductToCartOrAddQty(product_id, user_id, invoice);
		return new ResponseEntity<Orders>(invoice1, HttpStatus.CREATED);
	}
	
	@PostMapping("/subProduct/{product_id}/{user_id}")
	public ResponseEntity<?> subProductFromCart(@Valid @RequestBody Orders invoice, BindingResult result,
			 							@PathVariable Long product_id, @PathVariable Long user_id){
				
		
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Orders invoice1 = cartService.addProductToCartOrAddQty(product_id, user_id, invoice);
		return new ResponseEntity<Orders>(invoice1, HttpStatus.CREATED);
	}
	
	//user cek keranjang belanjaan dia sendiri
	@GetMapping("/getCartDetail/{track_order}")
	public Iterable<Cart> loadItemWantToBuy(@PathVariable String track_order){
		return cartService.getCartDetailByOrderIdentifier(track_order);		
	}

	//delete product di cart, bukan delete product
	@DeleteMapping("/deleteProductFromCart/{product_id}/{track_order}")
	public ResponseEntity<?> deleteProductFromCart(@PathVariable String track_order,
				@PathVariable Long product_id) {
		List<Cart> cart1 = cartService.removeProductFromCart(product_id, track_order);
		
		return new ResponseEntity<List<Cart>>(cart1, HttpStatus.OK);
	}

}
