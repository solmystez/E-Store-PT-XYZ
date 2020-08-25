package com.demo.lookopediaSinarmas.web;

import java.util.List;

import javax.validation.Valid;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.lookopediaSinarmas.domain.CartDetail;
import com.demo.lookopediaSinarmas.domain.Order;
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
	
	//addProduct to invoice With ProductId
	@PostMapping("/addProductToInvoice/{product_id}/{user_id}")
	public ResponseEntity<?> addProductToCartOrAddQty(@Valid @RequestBody Order invoice, BindingResult result,
			 							@PathVariable Long product_id, @PathVariable Long user_id){
				
		
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Order invoice1 = cartService.addProductToCartOrAddQty(product_id, user_id, invoice);
		return new ResponseEntity<Order>(invoice1, HttpStatus.CREATED);
	}
	
	@PostMapping("/subProductToInvoice/{product_id}/{user_id}")
	public ResponseEntity<?> subProductFromCart(@Valid @RequestBody Order invoice, BindingResult result,
			 							@PathVariable Long product_id, @PathVariable Long user_id){
				
		
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Order invoice1 = cartService.addProductToCartOrAddQty(product_id, user_id, invoice);
		return new ResponseEntity<Order>(invoice1, HttpStatus.CREATED);
	}
	
	//user cek keranjang belanjaan dia sendiri
	@GetMapping("/getCartDetail/{invoice_now}")
	public Iterable<CartDetail> loadItemWantToBuy(@PathVariable String invoice_now){
		return cartService.getCartDetailByInvoiceIdentifier(invoice_now);		
	}

	//delete product di cart, bukan delete product
	@DeleteMapping("/deleteProductFromCart/{product_id}/{invoiceIdentifier}")
	public ResponseEntity<?> deleteProductFromCart(@PathVariable String invoiceIdentifier,
				@PathVariable Long product_id) {
		List<CartDetail> cart1 = cartService.removeProductFromCart(product_id, invoiceIdentifier);
		
		return new ResponseEntity<List<CartDetail>>(cart1, HttpStatus.OK);
	}

}
