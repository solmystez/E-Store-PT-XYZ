package com.demo.eStore.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.eStore.entity.Category;
import com.demo.eStore.services.CategoryService;
import com.demo.eStore.services.otherService.MapValidationErrorService;

@CrossOrigin
@RestController
@RequestMapping("/api/category")
public class CategoryController {

	@Autowired 
	private CategoryService categoryService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	@PostMapping("/saveCategory")
	public ResponseEntity<?> saveCategoryData(@Valid @RequestBody Category category, BindingResult result){
		
		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Category category1 = categoryService.saveCategory(category);
		return new ResponseEntity<Category>(category1, HttpStatus.CREATED);
	}
	
	@GetMapping("/loadCategory")
	public Iterable<Category> loadAllCategory(){
		return categoryService.loadAllCategoryList();
	}
	
	@DeleteMapping("/deleteCategory/{category_id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long category_id){
		
		categoryService.deleteCategoryById(category_id);

		return new ResponseEntity<String>("Category ID '" + category_id  + "' was successfully deleted", HttpStatus.OK);
	}
}
