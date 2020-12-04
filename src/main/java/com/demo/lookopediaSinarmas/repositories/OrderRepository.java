package com.demo.lookopediaSinarmas.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.entity.Cart;
import com.demo.lookopediaSinarmas.entity.Orders;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Orders, Long>{
	
//	@Query(value = "SELECT e FROM Invoice i WHERE e.invoice_identifier =: invoice_dentifier", nativeQuery = true)
	Orders findByOrderIdentifier(String orderIdentifier);	
	Orders findAllByOrderIdentifier(String orderIdentifier, Sort sort);	
	
	Orders findByUserId(Long id);
	
	@Query(value = "select * from orders "
			+ "where merchant_name=:merchant_name "
			+ " and "
			+ " username=:username "
			+ " and "
			+ " status=:status"
			, nativeQuery = true)
	List<Orders> findByMerchantNameAndUsernameAndStatusIsNull(
			@Param("merchant_name") String merchant_name, 
			@Param("username") String username,
			@Param("status") String status);
	
	List<Orders> findAllByUserId(Long user_id);
	List<Orders> findAllByMerchantName(String merchant_name);
	
}
