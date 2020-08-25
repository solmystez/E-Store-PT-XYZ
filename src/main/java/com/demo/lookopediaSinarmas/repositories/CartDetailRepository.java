package com.demo.lookopediaSinarmas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.domain.CartDetail;
@Repository
public interface CartDetailRepository extends CrudRepository<CartDetail, Long>{
	
	//opsi 1
	//delete return nya void
	//findall -> pencet delete-> delete service(delete  findall) ->list all di update
//	@Modifying
//	@Query(value = "delete from cart_detail  "
//			+ " where order_identifier=:order_identifier "
//			+ " and "
//			+ " product_id=:product_id", nativeQuery = true)
//	void deleteCartDetailByInvoiceIdentifierAndProductId(
//			@Param("order_identifier") String invoiceIdentifier, 
//			@Param("product_id") Long productId);
	
	//opsi 2

	
	
	
	CartDetail findByOrderIdentifier(String orderIdentifier);

	List<CartDetail> findAllByOrderIdentifier(String invoice_now);
	
	List<CartDetail> findAllCartDetailsByOrderId(Long id);
	
	List<CartDetail> deleteAllByOrderId(Long id);
	
	CartDetail findAllById(Long cartDetail_id);
	
	
}
