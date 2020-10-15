package com.demo.lookopediaSinarmas.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

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

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
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
	
	
	@GetMapping(value = "/findProduct/{product_id}",
			produces = {MediaType.IMAGE_JPEG_VALUE,
					MediaType.IMAGE_GIF_VALUE,
					MediaType.IMAGE_PNG_VALUE})
	public ResponseEntity<Resource> findSpecificProduct(
			@PathVariable Long product_id,
//			@PathVariable String filename,
			HttpServletRequest request) {
		
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
			//ensure that is binary file
			contentType = "application/octet-stream";
		}
		
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
//			    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
				.body(resource);
	}
	
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
	
	
	@DeleteMapping("/deleteProduct/{product_id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long product_id){
		
		productService.deleteProductById(product_id);

		return new ResponseEntity<String>("Product ID '" + product_id  + "' was successfully deleted", HttpStatus.OK);
	}
}
