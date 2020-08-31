package com.demo.lookopediaSinarmas.exceptions;

public class CategoryAlreadyExistsResponse {
	
	private String categoryName;

	public CategoryAlreadyExistsResponse(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
}
