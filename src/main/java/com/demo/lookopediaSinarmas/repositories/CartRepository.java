package com.demo.lookopediaSinarmas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.entity.Cart;
@Repository
public interface CartRepository extends CrudRepository<Cart, Long>{
	

	//delete return nya void
	//findall -> pencet delete-> delete service(delete  findall) ->list all di update
	@Modifying
	@Query(value = "delete from cart  "
			+ " where order_identifier=:order_identifier "
			+ " and "
			+ " product_id=:product_id", nativeQuery = true)
	void deleteCartDetailByOrderIdentifierAndProductId(
			@Param("order_identifier") String invoiceIdentifier, 
			@Param("product_id") Long productId);
	
	Cart findByOrderIdentifier(String orderIdentifier);

	List<Cart> findAllByOrderIdentifier(String invoice_now);
	
	List<Cart> deleteAllByOrderId(Long id);
	
	
}
