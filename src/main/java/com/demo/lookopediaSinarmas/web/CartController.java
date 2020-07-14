package com.demo.lookopediaSinarmas.web;

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
import com.demo.lookopediaSinarmas.domain.CartDetail;
import com.demo.lookopediaSinarmas.domain.Invoice;
import com.demo.lookopediaSinarmas.services.CartService;
import com.demo.lookopediaSinarmas.services.MapValidationErrorService;

@CrossOrigin
@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	

	
//	@PostMapping("/subProductToCartIdWithProductId/{product_id}")
//	public ResponseEntity<?> subProductToCartOrAddQty(@Valid @RequestBody Cart cart, BindingResult result,
//			 							@PathVariable Long product_id){
//				
//		
//		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
//		if(mapError != null) return mapError;
//		
//		Cart cart1 = cartService.subQtyProductFromCartId(product_id, cart);
//		return new ResponseEntity<Cart>(cart1, HttpStatus.CREATED);
//	}
	
//	@DeleteMapping("/deleteProductIdFromCartId/{projectId}")
//	public ResponseEntity<?> deleteProductFromCartOrSubQty(@Valid @RequestBody Cart cart, BindingResult result,
//				@PathVariable Long product_id) {
//		Cart cart1 = cartService.removeProductFromCart(product_id, cart);
//		
//		return new ResponseEntity<Cart>(cart1, HttpStatus.OK);
//		
//	}
	
//	@PostMapping("/buy/{cart_id}")
//	public ResponseEntity<?> processItem(@Valid @RequestBody CartDetail cartDetail, BindingResult result,@PathVariable Long cart_id){
//		
//		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
//		if(mapError != null) return mapError;
//		CartDetail cartDetail1 = cartService.processItem(cart_id, cartDetail);
//				
//		return new ResponseEntity<CartDetail>(cartDetail1, HttpStatus.CREATED);	
//	}
	
	//user cek keranjang belanjaan dia sendiri
	@GetMapping("/getCartDetailByCartId/{cart_id}")
	public Iterable<CartDetail> loadItemWantToBuy(@PathVariable Long cart_id){
		return cartService.getCartDetailByCartId(cart_id);		
	}
	
}
