package com.demo.lookopediaSinarmas.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.lookopediaSinarmas.entity.Product;
import com.demo.lookopediaSinarmas.services.ProductService;
import com.demo.lookopediaSinarmas.services.image.ImageStorageService;

@CrossOrigin
@RestController
@RequestMapping("/api/product")
public class ProductController {
		
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ImageStorageService imageStorageService;
	
	@GetMapping("/findProductByCategory/{category_name}")
	public Iterable<Product> loadMerchantProduct(@PathVariable String category_name){
		return productService.findProductByCategory(category_name);
	}
	
	@GetMapping("/findProduct/{product_id:.+}")
	public ResponseEntity<Resource> findSpecificProduct(@PathVariable Long product_id
			, HttpServletRequest request) {
		
		Product product = productService.findProductById(product_id);
		
		String fileName = product.getFileName();
		
		Resource resource = imageStorageService.loadFileAsResource(fileName);
		
		String contentType = null;
		
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		}catch (IOException e) {
			System.out.println("cannot determine fileType");
		}
		
		if(contentType == null) {
			contentType = "application/octet-stream";
		}
		
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.body(resource);
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
}
