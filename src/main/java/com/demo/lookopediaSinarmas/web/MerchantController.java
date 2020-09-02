package com.demo.lookopediaSinarmas.web;

import java.security.Principal;

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

import com.demo.lookopediaSinarmas.domain.Merchant;
import com.demo.lookopediaSinarmas.domain.Product;
import com.demo.lookopediaSinarmas.domain.User;
import com.demo.lookopediaSinarmas.services.MerchantService;
import com.demo.lookopediaSinarmas.services.ProductService;
import com.demo.lookopediaSinarmas.services.otherService.MapValidationErrorService;

@CrossOrigin
@RestController
@RequestMapping("/api/merchant")
public class MerchantController {
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private MerchantService merchantService;
	
	
	@PostMapping("/createMerchantToUserId/{user_id}")
	public ResponseEntity<?> createMerchant(@Valid @RequestBody Merchant merchant, 
		BindingResult result,@PathVariable Long user_id, Principal principal){

		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Merchant merchant1 = merchantService.createMerchant(user_id, merchant, principal.getName());
		return new ResponseEntity<Merchant>(merchant1, HttpStatus.CREATED);
	 }
	
	
	@PostMapping("/createProduct/{merchant_id}")
	public ResponseEntity<?> createNewProduct(@Valid @RequestBody Product product, 
		BindingResult result, @PathVariable Long merchant_id, Principal principal){

		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Product product1 = productService.createProduct(merchant_id, product, principal.getName());
		return new ResponseEntity<Product>(product1, HttpStatus.CREATED);
	}
	
	@PostMapping("/updateProduct/{merchant_id}")
	public ResponseEntity<?> updateExistProduct(@Valid @RequestBody Product product, 
		BindingResult result, @PathVariable Long merchant_id, Principal principal){

		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Product product1 = productService.updateProduct(merchant_id, product, principal.getName());
		return new ResponseEntity<Product>(product1, HttpStatus.CREATED);
	 }
	
	@GetMapping("/findMerchant/{user_id}")
	public ResponseEntity<?> findMerchantByUserId(@PathVariable Long user_id) {
		
		Merchant merchant = merchantService.findMerchantByUserId(user_id);
		
		return new ResponseEntity<Merchant>(merchant, HttpStatus.OK);
	}
	
	@GetMapping("/loadMerchantProduct/{merchant_id}")
	public Iterable<Product> loadMerchantProduct(@PathVariable Long merchant_id){
		return productService.findAllProductsByMerchantId(merchant_id);
	}

}
