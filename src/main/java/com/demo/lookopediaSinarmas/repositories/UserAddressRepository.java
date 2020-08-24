package com.demo.lookopediaSinarmas.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.domain.Address;

@Repository
public interface UserAddressRepository extends CrudRepository<Address, Long> {
	
}
