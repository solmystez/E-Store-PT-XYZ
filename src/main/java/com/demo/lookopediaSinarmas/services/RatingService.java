package com.demo.lookopediaSinarmas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.entity.Comment;
import com.demo.lookopediaSinarmas.entity.Product;
import com.demo.lookopediaSinarmas.entity.Rating;
import com.demo.lookopediaSinarmas.entity.User;
import com.demo.lookopediaSinarmas.exceptions.product.ProductNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.user.UserIdNotFoundException;
import com.demo.lookopediaSinarmas.repositories.ProductRepository;
import com.demo.lookopediaSinarmas.repositories.RatingRepository;
import com.demo.lookopediaSinarmas.repositories.UserRepository;

@Service
public class RatingService {

	@Autowired
	RatingRepository ratingRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	public Rating postComment(Rating rating, Long product_id, Long user_id) {
		
		Product product;
		try {
			product = productRepository.findById(product_id).get();
		} catch (Exception e) {
			throw new ProductNotFoundException("Product not found");
		}
		
		User user;
		try {
			user = userRepository.findById(user_id).get();
		} catch (Exception e) {
			throw new UserIdNotFoundException("User not found");
		}
		
		rating.setRatingValue(rating.getRatingValue());
		rating.setProductRating(product);
		rating.setUserRating(user);
		
		return ratingRepository.save(rating);
	}
}
