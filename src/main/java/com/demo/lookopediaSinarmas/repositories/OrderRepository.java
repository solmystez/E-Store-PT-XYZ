package com.demo.lookopediaSinarmas.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.domain.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long>{
	
//	@Query(value = "SELECT e FROM Invoice i WHERE e.invoice_identifier =: invoice_dentifier", nativeQuery = true)
	Order findByOrderIdentifier(String orderIdentifier);	
	
	Order findByUserId(Long id);
	
	List<Order> findAllByUserId(Long user_id);
	
}
