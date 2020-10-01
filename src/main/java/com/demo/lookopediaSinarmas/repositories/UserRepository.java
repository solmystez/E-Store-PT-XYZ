package com.demo.lookopediaSinarmas.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.domain.Orders;
import com.demo.lookopediaSinarmas.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	
	//good thing about optional object, it prevents no point or exception
//	User findByUsername(String username);
	User findByEmail(String email);//uniq
	User getById(Long id);
	Orders findByTrackOrder(String orderIdentifier);
	
//	@Transactional
//	@Modifying
//	@Query("delete from User u where u.id = like ?1 and profileFilePicture like ?2")
//	public void deleteUserWithFile(Long id, String fileName);
}
