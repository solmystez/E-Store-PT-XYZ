package com.demo.eStore.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.eStore.entity.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long>{

	List<Product> findAllProductsByMerchantId(Long id);
	
//	List<Product> findAllProductsByMerchantId(Long id);
	
	List<Product> findAllProductByProductCategoryName(String categoryName);

	Iterable<Product> findAllByMerchantName(String merchantName);
}
