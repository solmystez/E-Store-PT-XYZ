package com.demo.lookopediaSinarmas.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	
	//good thing about optional object, it prevents no point or exception
//	User findByUsername(String username);
	User findByEmail(String email);//uniq
	User getById(Long id);
	boolean existsByEmail(String email);
	boolean existsByUsername(String username);
	User findByUsername(String username);
}
