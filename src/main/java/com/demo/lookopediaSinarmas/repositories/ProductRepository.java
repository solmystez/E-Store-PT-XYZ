package com.demo.lookopediaSinarmas.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.domain.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long>{

//	List<Product> findByCart(String cart_id);
//	
//	Product findByProductIdentifier(String product_id);
	
	List<Product> findAllProductsByMerchantId(Long id);

	List<Product> findProductsByProductCategory(String categoryName);

	Iterable<Product> findAllByMerchantName(String merchantName);
}
