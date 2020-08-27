package com.demo.lookopediaSinarmas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.domain.Address;
import com.demo.lookopediaSinarmas.domain.User;

@Repository
public interface UserAddressRepository extends CrudRepository<Address, Long> {
	
	List<Address> findByAddress(String address);	

//	@Query(value = "select * from Address "
//			+ "where user_id=:user_id", nativeQuery = true)
//	Address findAllAddressByUserId(@Param("user_id") Long user_id);

}
