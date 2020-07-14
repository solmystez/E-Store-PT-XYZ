package com.demo.lookopediaSinarmas.repositories;

import org.springframework.data.repository.CrudRepository;

import com.demo.lookopediaSinarmas.domain.Merchant;
import com.demo.lookopediaSinarmas.domain.User;


public interface MerchantRepository extends CrudRepository<Merchant, Long>{

//	List<Product> findByUserIdentifier(String identifier);
	
//	Merchant findByUserId(Long id);
	
//	Merchant findByUser_merchant(String id);

//	Merchant findByMerchantIdentifier(String merchantIdentifier);
	
	Merchant findByMerchantName(String merchantName);
	
	Merchant findMerchantById(Long id);
}
