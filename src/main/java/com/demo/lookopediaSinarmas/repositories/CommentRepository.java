package com.demo.lookopediaSinarmas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.domain.Comment;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long>{

//	List<Comment> findAllByProductId(Long product_id);

//	@Modifying
//	@Query(value = "delete from comment "
//			+ "where product_id:=product_id "
//			+ "and "
//			+ "user_id=:user_id", nativeQuery = true)
//	void deleteCommentByUserIdAndProductId(
//			@Param("product_id") Long productId,
//			@Param("user_id") Long userId);

//	If you want to provide an object as param, you can do something like this.
//	@Query("UPDATE Entity E SET E.name = :#{#entity.name}")
//	public void updateEntity(@Param("entity") Entity entity);
}
