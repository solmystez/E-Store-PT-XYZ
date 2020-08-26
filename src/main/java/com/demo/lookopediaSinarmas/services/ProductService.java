package com.demo.lookopediaSinarmas.services;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.CartDetail;
import com.demo.lookopediaSinarmas.domain.Order;
import com.demo.lookopediaSinarmas.domain.Merchant;
import com.demo.lookopediaSinarmas.domain.Product;
import com.demo.lookopediaSinarmas.domain.User;
import com.demo.lookopediaSinarmas.exceptions.OrderNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.MerchantNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.ProductIdException;
import com.demo.lookopediaSinarmas.exceptions.ProductNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.UserIdNotFoundException;
import com.demo.lookopediaSinarmas.repositories.CartDetailRepository;
import com.demo.lookopediaSinarmas.repositories.OrderRepository;
import com.demo.lookopediaSinarmas.repositories.MerchantRepository;
import com.demo.lookopediaSinarmas.repositories.ProductRepository;
import com.demo.lookopediaSinarmas.repositories.UserRepository;

@Service
public class ProductService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	MerchantRepository merchantRepository;


	
	
	public Product createProduct(Long merchantId, Product product, String merchantName) {

		try {
		    Merchant merchant = merchantRepository.findById(merchantId).get();
//		    Merchant merchant1 = merchantRepository.findByMerchantName(merchantName);
		    
		    
		    product.setMerchant(merchant);
		    product.setMerchantName(merchant.getMerchantName());
		    product.setProductCategory(product.getProductCategory().toLowerCase());
		    
		    Integer totalProduct = merchant.getTotalProduct();
		    totalProduct++;		    
		    merchant.setTotalProduct(totalProduct);
		    
				
			return productRepository.save(product);
		} catch (Exception e) {
			throw new MerchantNotFoundException("merchant not found");
		}
	}
	
	
	public Product updateProduct(Long merchantId, Product product, String merchantName) {
		//when we trying to update the project that doesn't belong to that user
		//project != null, find by db id -> null

		if(product.getId() != null) {
			Product existingProduct = productRepository.findById(product.getId()).get();
			
			if(existingProduct != null && (!existingProduct.getMerchantName().equals(merchantName))) {
				throw new ProductNotFoundException("Product not found in your merchant");
			}else if (existingProduct == null) {
				throw new ProductNotFoundException("Product '" + product.getProductName() + "' cannot updated, because it doesn't exist");
			}
		}

		
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
