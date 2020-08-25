package com.demo.lookopediaSinarmas.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.domain.Order;
import com.demo.lookopediaSinarmas.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	
	//good thing about optional object, it prevents no point or exception
//	User findByUsername(String username);
	User findByEmail(String email);//uniq
	User getById(Long id);
	Order findByOrderNow(String orderIdentifier);
}
