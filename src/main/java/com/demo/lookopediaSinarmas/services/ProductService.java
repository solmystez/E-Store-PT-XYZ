package com.demo.lookopediaSinarmas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.Merchant;
import com.demo.lookopediaSinarmas.domain.Product;
import com.demo.lookopediaSinarmas.exceptions.MerchantNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.ProductIdException;
import com.demo.lookopediaSinarmas.repositories.CartRepository;
import com.demo.lookopediaSinarmas.repositories.MerchantRepository;
import com.demo.lookopediaSinarmas.repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	MerchantRepository merchantRepository;

	public Product createProduct(Long merchantId, Product product) {
		try {
		    Merchant merchant = merchantRepository.findById(merchantId).get();
		    
		    Integer totalProduct = merchant.getTotalProduct();
		    totalProduct++;		    
		    merchant.setTotalProduct(totalProduct);
		    
			product.setMerchant(merchant);
			product.setProductCategory(product.getProductCategory().toLowerCase());
				
			return productRepository.save(product);
		} catch (Exception e) {
			throw new MerchantNotFoundException("merchant not found");
		}
	}
	
	
	public Product updateProduct(Long merchantId, Product product) {
		try {
		    Merchant merchant = merchantRepository.findById(merchantId).get();
		    	    
			product.setMerchant(merchant);
			product.setProductCategory(product.getProductCategory().toLowerCase());
			
			return productRepository.save(product);
		} catch (Exception e) {
			throw new MerchantNotFoundException("merchant not found");
		}

	}
	

	public Iterable<Product> findAllProducts() {
		return productRepository.findAll(); 
	}
	
	public List<Product> findAllProductsByMerchantId(Long id) {
		return productRepository.findAllProductsByMerchantId(id); 
	}
	
	public List<Product> findProductByCategory(String categoryName) {
		return productRepository.findProductsByProductCategory(categoryName.toLowerCase()); 
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
