package com.demo.lookopediaSinarmas.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.entity.Cart;
@Repository
public interface CartRepository extends PagingAndSortingRepository<Cart, Long>{
	

	//delete return nya void
	//findall -> pencet delete-> delete service(delete  findall) ->list all di update
	@Modifying
	@Query(value = "delete from cart  "
			+ " where order_identifier=:order_identifier "
			+ " and "
			+ " product_id=:product_id", nativeQuery = true)
	void deleteCartDetailByOrderIdentifierAndProductId(
			@Param("order_identifier") String order_identifier, 
			@Param("product_id") Long productId);
	
	@Query(value = "select * from cart "
			+ "where order_identifier=:order_identifier "
			+ "and "
			+ "merchant_name=:merchant_name", nativeQuery = true)
	List<Cart> selectCourierByOrderIdentifierAndMerchantName(
			@Param("order_identifier") String order_identifier, 
			@Param("merchant_name") String merchant_name);
	
	@Query(value = "select * from cart "
			+ "where order_id=:order_id " + 
			"	and " + 
			" product_id=:product_id", nativeQuery = true)
	List<Cart> selectCourierByOrderIdAndProductId(
			@Param("order_id") Long order_id, 
			@Param("product_id") Long product_id);
	//findAllAndSortbyMerchanName
	List<Cart> findAllByOrderIdentifier(String orderIdentifier, Sort sort);

	List<Cart> findAllByUsername(String username, Sort sort);
	
	//view product
	ArrayList<Cart> findAllByUsernameAndStatus(String username, String status);
	
	Cart findByUsernameAndStatus(String username, String status);
	
	//for load all product that should be acc/rejected
	@Query(value = "select * from cart "
			+ "where order_identifier=:order_identifier "
			+ "and "
			+ "product_id=:product_id", nativeQuery = true)
	Cart setStatusProductWithOrderIdentifierAndProductId(
			@Param("order_identifier") String order_identifier, 
			@Param("product_id") Long productId);
	
//	List<Cart> findAllByMerchantNameAndStatus(String merchantName, String status);
	
	Cart findByOrderIdentifier(String orderIdentifier);

	List<Cart> findAllByOrderIdentifier(String invoice_now);
	
	List<Cart> deleteAllByOrderId(Long id);
	
	List<Cart> findAllByUsername(String username);

	
}
