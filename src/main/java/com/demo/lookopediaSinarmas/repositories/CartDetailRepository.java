package com.demo.lookopediaSinarmas.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.domain.CartDetail;
@Repository
public interface CartDetailRepository extends CrudRepository<CartDetail, Long>{
	
	CartDetail findByCartId(Long id);
	List<CartDetail> findAllByCartId(Long id);
	
	List<CartDetail> findAllCartDetailsByCartId(Long id);
	
	List<CartDetail> deleteAllByCartId(Long id);
	
	CartDetail findAllById(Long cartDetail_id);
	
}
