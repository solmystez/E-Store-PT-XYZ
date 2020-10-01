package com.demo.lookopediaSinarmas.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.domain.Merchant;

@Repository
public interface MerchantRepository extends CrudRepository<Merchant, Long>{

	
//	@Query("select u from Merchant u where u.user_id = :user_id")
//	Merchant findByUserId(@Param("user_id") Long user_id);
	
	Merchant findMerchantByUserMerchantId(Long id);
	
//	Merchant getMerchantByUserId (Long id);
	
	Merchant findByMerchantName(String merchantName);

	
	
}
