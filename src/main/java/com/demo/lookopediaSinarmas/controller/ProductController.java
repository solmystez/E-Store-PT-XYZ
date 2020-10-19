package com.demo.lookopediaSinarmas.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.demo.lookopediaSinarmas.entity.Merchant;
import com.demo.lookopediaSinarmas.entity.Product;
import com.demo.lookopediaSinarmas.exceptions.merchant.MerchantNameAlreadyExistsException;
import com.demo.lookopediaSinarmas.exceptions.merchant.MerchantNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.product.ProductNotFoundException;
import com.demo.lookopediaSinarmas.repositories.MerchantRepository;
import com.demo.lookopediaSinarmas.repositories.ProductRepository;
import com.demo.lookopediaSinarmas.services.ProductService;
import com.demo.lookopediaSinarmas.services.image.ImageStorageService;
import com.demo.lookopediaSinarmas.services.otherService.MapValidationErrorService;

@CrossOrigin(origins = { "http://localhost:3000"})
@RestController
@RequestMapping("/api/product")
public class ProductController {
		
	private static Logger log = LoggerFactory.getLogger(MerchantController.class);
	public static String uploadDirectory = System.getProperty("user.dir") +  "/uploads";
		
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ImageStorageService imageStorageService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	MerchantRepository merchantRepository;
	
	@PostMapping("/createProduct/{merchant_id}")
	public @ResponseBody ResponseEntity<?> createNewProduct(@Valid Product product, 
		BindingResult result, Principal principal, @PathVariable Long merchant_id,
//		@RequestParam("name") final String name, //tambahin nnti principal validation
		@RequestParam(value = "file", required = false) MultipartFile file) throws FileNotFoundException{

		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;

		Merchant merchant = merchantRepository.findMerchantByUserMerchantId(merchant_id);
		if(merchant == null) {
			throw new MerchantNotFoundException("Merchant not found");		    	
		}
		
//		String fileName;
//		try {
//			fileName = imageStorageService.storeFile(file);
//		} catch (Exception e) {
//			throw new FileNotFoundException("please insert image");
//		}
		
		if(file == null) {
			return new ResponseEntity<String>("please insert image", HttpStatus.BAD_REQUEST);
		}
		String fileName = imageStorageService.storeFile(file);
		
		
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/product/loadImageProduct/")
				.path(fileName)
				.toUriString();
		
		String productFileName = file.getOriginalFilename();
		String productFilePath = Paths.get(uploadDirectory, productFileName).toString();
		String productFileType = file.getContentType();
		long size = file.getSize();
		String productFileSize = String.valueOf(size);
		
		product.setProductName(product.getProductName());
		product.setProductDescription(product.getProductDescription());
		product.setProductCategoryName(product.getProductCategoryName());
		product.setProductPrice(product.getProductPrice());
		product.setProductStock(product.getProductStock());
		
		product.setMerchant(merchant);
		product.setProductCategory(product.getProductCategory());
		
		product.setFileName(productFileName);
		product.setFilePath(fileDownloadUri);//fileDownloadUri, productFilePath
		product.setFileType(productFileType);
		product.setFileSize(productFileSize);
		
		log.info("product created");
		productRepository.save(product);
//		Product product1 = productService.createProduct(merchant_id, product, principal.getName());
		return new ResponseEntity<Product>(HttpStatus.CREATED);
	
	}
	
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
	
	@PostMapping("/updateProduct/{merchant_id}")
	public ResponseEntity<?> updateExistProduct(@Valid @RequestBody Product product, 
		BindingResult result, @PathVariable Long merchant_id, Principal principal){
		
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Product product1 = productService.updateProduct(merchant_id, product, principal.getName());
		return new ResponseEntity<Product>(product1, HttpStatus.CREATED);
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
