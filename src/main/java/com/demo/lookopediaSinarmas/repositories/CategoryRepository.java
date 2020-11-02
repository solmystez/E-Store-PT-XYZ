package com.demo.lookopediaSinarmas.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.lookopediaSinarmas.entity.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long>{

	Category findByCategoryName(String categoryName);
}
