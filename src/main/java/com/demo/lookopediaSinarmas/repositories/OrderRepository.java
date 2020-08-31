package com.demo.lookopediaSinarmas.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.domain.Orders;

@Repository
public interface OrderRepository extends CrudRepository<Orders, Long>{
	
//	@Query(value = "SELECT e FROM Invoice i WHERE e.invoice_identifier =: invoice_dentifier", nativeQuery = true)
	Orders findByOrderIdentifier(String orderIdentifier);	
	
	Orders findByUserId(Long id);
	
	List<Orders> findAllByUserId(Long user_id);
	
}
