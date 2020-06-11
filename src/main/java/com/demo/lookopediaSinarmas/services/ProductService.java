package com.demo.lookopediaSinarmas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.Merchant;
import com.demo.lookopediaSinarmas.domain.Product;
import com.demo.lookopediaSinarmas.exceptions.MerchantNotFoundException;
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

	public Product createOrUpdateProduct(Long merchantId, Product product) {

		//Logic
		//we didn't create cart that don't belong to nothing
		try {

			if(product.getId() == null) {
			    Merchant merchant = merchantRepository.findById(merchantId).get();
				product.setMerchant(merchant);
				
			}
			Product newProduct = productRepository.save(product);
			return newProduct;
		} catch (Exception e) {
			throw new MerchantNotFoundException("merchant not found");
		}

	}

	//	public Product findProductByCategory(String productId) {
	//		
	//		Product product = productRepository.findByProductIdentifier(productId.toUpperCase());
	//
	//		if(product == null) {
	//			throw new ProductIdException("Product ID '" + productId +"' doesn't exists");
	//		}
	//		
	//		return product;
	//	}


	public Iterable<Product> findAllProducts() {
		return productRepository.findAll(); 
	}

	public void deleteProductByIdentifier(String productId) {

		//		Product product = productRepository.findByProductIdentifier(productId.toUpperCase());
		//		
		//		if(product == null) {
		//			throw new ProductIdException("Product ID '" + productId +"' cannot delete because doesn't exists");
		//		}
		//		productRepository.delete(product);
		//		
	}
}
