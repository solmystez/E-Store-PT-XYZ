package com.demo.eStore.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.eStore.entity.Address;
import com.demo.eStore.entity.User;

@Repository
public interface UserAddressRepository extends CrudRepository<Address, Long> {

//	List<Address> findAllAddressByUserAddressId(Long user_id);
	
//	List<Address> findByUser_Id(Long id);
	
	@Query(value = "select * from address "
			+ " where username=:username", nativeQuery = true)
	List<Address> findAllAddressByUsername(@Param("username") String username);

}
