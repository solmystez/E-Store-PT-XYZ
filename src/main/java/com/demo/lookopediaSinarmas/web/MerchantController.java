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

import com.demo.lookopediaSinarmas.domain.Product;
import com.demo.lookopediaSinarmas.services.MapValidationErrorService;
import com.demo.lookopediaSinarmas.services.MerchantService;
import com.demo.lookopediaSinarmas.services.ProductService;

@CrossOrigin
@RestController
@RequestMapping("/api/merchant")
public class MerchantController {
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	@Autowired
	private MerchantService merchantService;
	
	@Autowired
	private ProductService productService;
	
	@PostMapping("/createProductToMerchantId/{merchant_id}")
	public ResponseEntity<?> createNewProduct(@Valid @RequestBody Product product, 
		BindingResult result, @PathVariable Long merchant_id){

		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Product product1 = productService.createOrUpdateProduct(merchant_id, product);
		return new ResponseEntity<Product>(product1, HttpStatus.CREATED);
	}
	
	@PostMapping("/deleteProductToMerchantId/{merchant_id}")
	public ResponseEntity<?> deleteProductFromCart(@Valid @RequestBody Product product, 
		BindingResult result, @PathVariable Long merchant_id){

		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Product product1 = productService.createOrUpdateProduct(merchant_id, product);
		return new ResponseEntity<Product>(product1, HttpStatus.CREATED);
	}
}
