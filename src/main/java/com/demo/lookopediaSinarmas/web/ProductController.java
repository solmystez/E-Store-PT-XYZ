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

import com.demo.lookopediaSinarmas.domain.Invoice;
import com.demo.lookopediaSinarmas.domain.Product;
import com.demo.lookopediaSinarmas.services.MapValidationErrorService;
import com.demo.lookopediaSinarmas.services.ProductService;

@CrossOrigin
@RestController
@RequestMapping("/api/product")
public class ProductController {
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	@Autowired
	private ProductService productService;
	
	
	//addProduct to invoiceId With ProductId
	@PostMapping("/addProductToInvoice/{product_id}/{user_id}")
	public ResponseEntity<?> addProductToCartOrAddQty(@Valid @RequestBody Invoice invoice, BindingResult result,
			 							@PathVariable Long product_id, @PathVariable Long user_id){
				
		
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Invoice invoice1 = productService.addProductToCartDetailOrAddQty(product_id, user_id, invoice);
		return new ResponseEntity<Invoice>(invoice1, HttpStatus.CREATED);
	}
	
	@GetMapping("/findProductByCategory/{category_name}")
	public Iterable<Product> loadMerchantProduct(@PathVariable String category_name){
		return productService.findProductByCategory(category_name);
	}
	
	@GetMapping("/loadAllProductOnCatalog")
	public Iterable<Product> loadAllProduct(){
		return productService.findAllProducts();
	}
	
	
	@DeleteMapping("/deleteProduct/{product_id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long product_id){
		
		productService.deleteProductById(product_id);

		return new ResponseEntity<String>("Product ID '" + product_id  + "' was successfully deleted", HttpStatus.OK);
	}
	
	@PostMapping("/processItem/{invoice_identifier}")
	public ResponseEntity<?> processItem(@Valid @RequestBody Invoice invoice,
			BindingResult result, @PathVariable String invoice_identifier) {
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
		if(errorMap != null) return errorMap;
		
		Invoice invoice1 = productService.processItem(invoice_identifier, invoice);
				
		return new ResponseEntity<Invoice>(invoice1, HttpStatus.CREATED);
	}
}
