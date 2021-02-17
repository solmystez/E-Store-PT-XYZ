package com.demo.eStore.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.eStore.entity.Product;
import com.demo.eStore.repositories.MerchantRepository;
import com.demo.eStore.services.ProductService;
import com.demo.eStore.services.image.ImageStorageService;
import com.demo.eStore.services.otherService.MapValidationErrorService;
import com.demo.eStore.validator.ProductValidator;

@CrossOrigin(origins = { "http://localhost:3000"})
@RestController
@RequestMapping("/api/product")
public class ProductController {
			
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ImageStorageService imageStorageService;
	
	@Autowired
	private ProductValidator productValidator;
	
	@Autowired
	MerchantRepository merchantRepository;
	
	@PostMapping("/createProduct/{merchant_id}")
	public @ResponseBody ResponseEntity<?> createNewProduct(@Valid Product product, 
		BindingResult result, Principal principal,
		@PathVariable Long merchant_id,
		@RequestPart("file") MultipartFile file){
		
		productValidator.validate(product, result);
		
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Product product1 = productService.createProduct(merchant_id, product, file, principal.getName());
		return new ResponseEntity<Product>(product1, HttpStatus.CREATED);
	
	}
	
	@PatchMapping("/updateProduct/{merchant_id}")
	public @ResponseBody ResponseEntity<?> updateRecentProduct(@Valid Product product, 
		BindingResult result, Principal principal,
		@PathVariable Long merchant_id,
		@RequestPart("file") MultipartFile file){
		
		productValidator.validate(product, result);
		
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Product product1 = productService.updateProductImgVer(merchant_id, product, file, principal.getName());
		return new ResponseEntity<Product>(product1, HttpStatus.CREATED);
	
	}
//	@PatchMapping("/updateProduct/{merchant_id}")
//	public ResponseEntity<?> updateExistProduct(@Valid @RequestBody Product product, 
//			BindingResult result, Principal principal,
//			@PathVariable Long merchant_id){
//		
////		productValidator.validate(product, result);
//		
//		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
//		if(mapError != null) return mapError;
//		
//		Product product1 = productService.updateProduct(merchant_id, product, principal.getName());
//		return new ResponseEntity<Product>(product1, HttpStatus.CREATED);
//	 }
	
//	@PatchMapping("/updateProductWithImage/{merchant_id}")
//	public ResponseEntity<?> updateExistProductWithImage(@Valid Product product, 
//			BindingResult result, Principal principal,
//			@PathVariable Long merchant_id,
//			MultipartFile file){
//		
//		productValidator.validate(product, result);
//		
//		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
//		if(mapError != null) return mapError;
//		
//		Product product1 = productService.updateProductVerMultipart(merchant_id, product,file, principal.getName());
//		return new ResponseEntity<Product>(product1, HttpStatus.CREATED);
//	 }
	
	@GetMapping("/findProductByCategory/{category_name}")
	public Iterable<Product> loadProductByCategoryName(@PathVariable String category_name){
		return productService.findAllProductByCategory(category_name);
	}
	
	@GetMapping(value = "/loadImageProduct/{filename:.+}",
			produces = {MediaType.IMAGE_JPEG_VALUE,
					MediaType.IMAGE_GIF_VALUE,
					MediaType.IMAGE_PNG_VALUE})
	public ResponseEntity<Resource> loadImageProduct(
			@PathVariable String filename,
			HttpServletRequest request) {
		
		Resource resource = imageStorageService.loadFileAsResource(filename);
		
		String contentType = null;
		
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		}catch (IOException e) {
			System.out.println("cannot determine fileType");
		}
		
		if(contentType == null) {
			//ensure that is binary file
			contentType = "application/octet-stream";
		}
		
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
//			    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
				.body(resource);
	}
	
	
//	@GetMapping("/findProduct/{product_id}")
//	public ResponseEntity<Resource> findSpecificProduct(
//			@PathVariable Long product_id,
////			@PathVariable String filename,
//			HttpServletRequest request) {
//		
//		Product product = productService.findProductById(product_id);
//		
//		String fileName = product.getFileName();
//		
//		Resource resource = imageStorageService.loadFileAsResource(fileName);
//		
//		String contentType = null;
//		
//		try {
//			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
//		}catch (IOException e) {
//			System.out.println("cannot determine fileType");
//		}
//		
//		if(contentType == null) {
//			//ensure that is binary file
//			contentType = "application/octet-stream";
//		}
//		
//		return ResponseEntity.ok()
//				.contentType(MediaType.parseMediaType(contentType))
////			    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
//				.body(resource);
//	}
	
//	public Stream<Path> loadAll(){
//		try {
//	      return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
//	    } catch (IOException e) {
//	      throw new RuntimeException("Could not load the files!");
//	    }
//	}
//	
//	@GetMapping("/loadAllProductMv")
//	public ResponseEntity<List<Product>> loadAllProductsMultipartVer(){
//		List<Product> allProduct = 
//		
//		return productService.findAllProducts();
//	}
	
	@GetMapping("/loadAllProductOnCatalog")
	public Iterable<Product> loadAllProduct(){
		return productService.findAllProducts();
	}
	
	@GetMapping("/loadSpecificProduct/{product_id}")
	public ResponseEntity<?> loadProduct(@PathVariable Long product_id){
		
		Product product = productService.findProductById(product_id);

		return new ResponseEntity<Product>(product, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteProduct/{product_id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long product_id){
		
		productService.deleteProductById(product_id);

		return new ResponseEntity<String>("Product ID '" + product_id  + "' was successfully deleted", HttpStatus.OK);
	}
}
