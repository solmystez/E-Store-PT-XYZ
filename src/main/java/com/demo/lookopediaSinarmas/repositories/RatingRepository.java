package com.demo.lookopediaSinarmas.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.domain.Rating;

@Repository
public interface RatingRepository extends CrudRepository<Rating, Long>{
	//loadAllRatingByProductId
}
