package com.demo.lookopediaSinarmas.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.entity.Cart;
@Repository
public interface CartRepository extends PagingAndSortingRepository<Cart, Long>{
	

	//delete return nya void
	//findall -> pencet delete-> delete service(delete  findall) ->list all di update
	@Modifying
	@Query(value = "delete from Cart c where"
			+ " c.p_id=:p_id"
			+ " and "
			+ " c.username=:username "
			+ " and "
			+ " c.status=:status")
	void deleteProductByProductIdAndUsernameAndStatus(
			@Param("p_id") Long p_id,
			@Param("username") String username, 
			@Param("status") String status);
	
	@Query(value = "select * from cart "
			+ " product_id=:product_id"
			+ " and "
			+ " where username=:username "
			+ " and "
			+ " status=:status", nativeQuery = true)
	List<Cart> selectAllByProductIdAndUsernameAndStatus(
			@Param("product_id") Long productId,
			@Param("username") String username, 
			@Param("status") String status);
	
	//for load all product that should be acc/rejected
//	@Query(value = "select * from cart "
//			+ "where order_identifier=:order_identifier "
//			+ "and "
//			+ "product_id=:product_id", nativeQuery = true)
//	Cart setStatusProductWithOrderIdentifierAndProductId(
//			@Param("order_identifier") String order_identifier, 
//			@Param("product_id") Long productId);
	
	@Query(value = "select * from cart "
			+ "where order_id=:order_id" + 
			" and " + 
			" product_id=:product_id", nativeQuery = true)
	List<Cart> selectCourierByOrderIdAndProductId(
			@Param("order_id") Long order_id, 
			@Param("product_id") Long product_id);
	
	@Query(value = "select * from cart "
			+ " where order_id=:order_id"
			+ " and "
			+ " username=:username "
			+ " and "
			+ " status=:status", nativeQuery = true)
	List<Cart> findAllByOrderIdUsernameAndStatus(
			@Param("order_id") Long order_id,
			@Param("username") String username, 
			@Param("status") String status);

	@Query(value = "select * from cart "
			+ " where order_id=:order_id"
			+ " and"
			+ " product_id=:product_id"
			+ " and"
			+ " status=:status", nativeQuery = true)
	Cart findByOrderIdProductIdAndStatus(
			@Param("order_id") Long order_id,
			@Param("product_id") Long product_id, 
			@Param("status") String status);
	
	//findAllAndSortbyMerchanName
	//view product
	List<Cart> findAllByUsernameAndStatus(String username, String status);
	
	Cart findByUsernameAndStatus(String username, String status);
	List<Cart> deleteAllByOrderId(Long id);
	List<Cart> findAllByUsername(String username);

	
}
