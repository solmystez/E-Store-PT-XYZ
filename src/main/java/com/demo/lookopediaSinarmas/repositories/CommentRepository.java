package com.demo.lookopediaSinarmas.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.domain.Comment;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long>{

}
