package com.demo.lookopediaSinarmas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.entity.Comment;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long>{

//	@Query(value = "Select * From comment c"
//			+ "where c.product_id=:product_id")
//	void findAllCommentByProductId(@Param("product_id") Long product_id);

//	List<Comment> findAllByProductId(Long id);
	
	@Modifying
	@Query(value = "delete from comment "
			+ "where product_id=:product_id "
			+ "and "
			+ "user_id=:user_id", nativeQuery = true)
	void deleteCommentByUserIdAndProductId(
			@Param("product_id") Long productId,
			@Param("user_id") Long userId);

	@Query(value = "select * from comment where"
			+ "product_id=:product_id"
			+ "and"
			+ "user_id=:user_id", nativeQuery = true)
	List<Comment> findAllCommentByProductIdAndUserId(
			@Param("product_id") Long productId,
			@Param("user_id") Long userId);
	
	Comment findByProductCommentProduct_idAndUserCommentId(Long product_id, Long user_id);
//	If you want to provide an object as param, you can do something like this.
//	@Query("UPDATE Entity E SET E.name = :#{#entity.name}")
//	public void updateEntity(@Param("entity") Entity entity);
}
