package com.demo.lookopediaSinarmas.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	
}
