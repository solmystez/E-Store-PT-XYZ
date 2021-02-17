package com.demo.eStore.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.eStore.entity.Category;
import com.demo.eStore.exceptions.category.CategoryAlreadyExistsException;
import com.demo.eStore.exceptions.product.ProductIdException;
import com.demo.eStore.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	CategoryRepository categoryRepository;
	
	public Category saveCategory(Category category) {
		try {
			category.setCategoryName(category.getCategoryName());			
			return categoryRepository.save(category);
		} catch (Exception e) {
			throw new CategoryAlreadyExistsException(category.getCategoryName() + " already exists");
		}
	}
	
	public Iterable<Category> loadAllCategoryList(){
		return categoryRepository.findAll();
	}
	
	public void deleteCategoryById(Long category_id) {
		
		try {
			Category category = categoryRepository.findById(category_id).get();
			 categoryRepository.delete(category);	
		} catch (Exception e) {
			throw new ProductIdException("Category with ID '" + category_id +"' cannot delete because doesn't exists");			
		}
	}
}
