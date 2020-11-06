package com.demo.lookopediaSinarmas.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.demo.lookopediaSinarmas.controller.MerchantController;
import com.demo.lookopediaSinarmas.entity.Category;
import com.demo.lookopediaSinarmas.entity.Merchant;
import com.demo.lookopediaSinarmas.entity.Product;
import com.demo.lookopediaSinarmas.exceptions.merchant.MerchantNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.product.ProductIdException;
import com.demo.lookopediaSinarmas.exceptions.product.ProductNotFoundException;
import com.demo.lookopediaSinarmas.repositories.CategoryRepository;
import com.demo.lookopediaSinarmas.repositories.MerchantRepository;
import com.demo.lookopediaSinarmas.repositories.ProductRepository;
import com.demo.lookopediaSinarmas.services.image.ImageStorageService;

@Service
public class ProductService {
	
	public static String uploadDirectory = System.getProperty("user.dir") +  "/uploads";
	private static Logger log = LoggerFactory.getLogger(MerchantController.class);
	
	@Autowired
	ProductRepository productRepository;

	@Autowired
	MerchantRepository merchantRepository;
	
	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	private ImageStorageService imageStorageService;
	
	public Product createProduct(Long merchant_id, Product product, MultipartFile file, String userName) {

//		if(file == null || file.isEmpty()) {	
//		result.addError(new ObjectError("fileName", "please insert image"));
//		throw new FileNotFoundException("please to store empty0 image");
//		return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
//	}
		
//		String fileName;
//		try {
//			fileName = imageStorageService.storeFile(file);
//		} catch (Exception e) {
//			throw new FileNotFoundException("please insert image");
//		}
		
	    Merchant merchant = merchantRepository.findMerchantByUserMerchantId(merchant_id);
	    if(merchant == null) throw new MerchantNotFoundException("Merchant not found");		    	
	    
	    if(!merchant.getUserMerchant().getUsername().equals(userName)) {
	    	throw new MerchantNotFoundException("cannot create product, wrong merchant_id parameter");
	    }
	    
	    String fileName = null;
    	
    	if(!file.isEmpty()) {
    		
    		fileName = imageStorageService.storeFile(file);
    	}else {
    		fileName = "nophoto.jpg";
    	}
    	String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
    			.path("/api/product/loadImageProduct/")
    			.path(fileName)
    			.toUriString();
    	
    	String productFileName = file.getOriginalFilename();
//			String productFilePath = Paths.get(uploadDirectory, productFileName).toString();
    	String productFileType = file.getContentType();
    	long size = file.getSize();
    	String productFileSize = String.valueOf(size);
    	try {		
    		product.setProductName(product.getProductName());
    		product.setProductDescription(product.getProductDescription());
    		product.setProductCategoryName(product.getProductCategoryName());
    		product.setProductPrice(product.getProductPrice());
    		product.setProductStock(product.getProductStock());
    		
    		product.setFileName(productFileName);
    		product.setFilePath(fileDownloadUri);//fileDownloadUri, productFilePath
    		product.setFileType(productFileType);
    		product.setFileSize(productFileSize);
    		
    		Category category = categoryRepository.findByCategoryName(product.getProductCategoryName()); 
    		
    		product.setMerchant(merchant);
    		product.setProductCategory(category);
    		product.setMerchantName(merchant.getMerchantName());
    		
    		Integer totalProduct = merchant.getTotalProduct();
    		totalProduct++;		    
    		merchant.setTotalProduct(totalProduct);
    		
    		log.info("product created");	
    		return productRepository.save(product);
    	} catch (Exception e) {
    		throw new MerchantNotFoundException("Merchant not found");
    	}
	}
	
	
	public Product updateProduct(Long merchantId, Product product, String merchantName) {
	
		//prevent update product that doesn't belong to this merchant
		if(product.getProduct_id() != null) {
			Product existingProduct = productRepository.findById(product.getProduct_id()).get();
			
			if(existingProduct != null && (!existingProduct.getMerchantName().equals(merchantName))) {
				throw new ProductNotFoundException("Product not found in your merchant");
			}else if (existingProduct == null) {
				throw new ProductNotFoundException("Product '" + product.getProductName() + "' cannot updated, because it doesn't exist");
			}
		}

		
		try {
		    Merchant merchant = merchantRepository.findById(merchantId).get();
		    	    
			product.setMerchant(merchant);
//			product.setProductCategory(product.getProductCategory().toLowerCase());
			
			return productRepository.save(product);
		} catch (Exception e) {
			throw new MerchantNotFoundException("merchant not found");
		}

	}
	
	//for findProduct
	//if (!product.getMerchantName.equal(merchant name)) throw new exception

	
	public Iterable<Product> findAllProducts() {
		return productRepository.findAll(); 
	}
	
	public List<Product> findAllProductsByMerchantId(Long id) {
		return productRepository.findAllProductsByMerchantId(id); 
	}
	
	public List<Product> findProductByCategory(String categoryName) {
		return productRepository.findProductsByProductCategory(categoryName.toLowerCase()); 
	}
	
	public Product findProductById(Long product_id) {
		
		Product product;
		
		try {
			product = productRepository.findById(product_id).get();
	
		} catch (Exception e) {
			throw new ProductIdException("Product with ID '" + product_id +"' cannot delete because doesn't exists");			
		}
		
		return product;
	}
	

	public void deleteProductById(Long product_id) {
		
		try {
			Product product = productRepository.findById(product_id).get();
			 productRepository.delete(product);	
		} catch (Exception e) {
			throw new ProductIdException("Product with ID '" + product_id +"' cannot delete because doesn't exists");			
		}
	}
	
	
}
