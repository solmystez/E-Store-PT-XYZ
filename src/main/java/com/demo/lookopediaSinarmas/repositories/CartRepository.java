package com.demo.lookopediaSinarmas.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.domain.Cart;
import com.demo.lookopediaSinarmas.domain.Product;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long>{
	
	Cart findByUserId(Long identifier);
	
	Cart save(Product product);
	
//	Cart findById(Long id);

	
}
