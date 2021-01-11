package com.demo.lookopediaSinarmas.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.entity.Orders;
import com.demo.lookopediaSinarmas.entity.Product;
import com.demo.lookopediaSinarmas.entity.RatingProduct;
import com.demo.lookopediaSinarmas.entity.User;
import com.demo.lookopediaSinarmas.exceptions.comment.CommentNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.product.ProductNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.user.UserIdNotFoundException;
import com.demo.lookopediaSinarmas.repositories.OrderRepository;
import com.demo.lookopediaSinarmas.repositories.ProductRepository;
import com.demo.lookopediaSinarmas.repositories.RatingProductRepository;
import com.demo.lookopediaSinarmas.repositories.UserRepository;

@Service
public class RatingProductService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	RatingProductRepository ratingProductRepository;
	
	public RatingProduct postRatingProduct(RatingProduct rating, Long product_id, Long user_id, Long order_id, String username) {
		
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
		
		Orders order = orderRepository.findById(order_id).get();
		order.setHasRating("Done");
		orderRepository.save(order);
		
		rating.setComment_message(rating.getComment_message());
		rating.setRatingValue(rating.getRatingValue());
		rating.setRatingProduct(product);
		rating.setUsername(username);
		rating.setUser(user);
		return ratingProductRepository.save(rating);
	}
	
	@Transactional
	public void removeRatingProductFromProduct(Long product_id, Long user_id) {
		try {
			ratingProductRepository.deleteRatingByUserIdAndProductId(product_id, user_id);
		} catch (Exception e) {
			throw new CommentNotFoundException("No Rating in this product found");
		}
	}

	public List<RatingProduct> getAllProductRatingInProductId(Long product_id) {
		
		List<RatingProduct> ratings;
		try {
			ratings = ratingProductRepository.findAllRatingByProductId(product_id);
			return ratings;
		} catch (Exception e) {
			throw new CommentNotFoundException("no rating found in this product id");
		}
		
	}
	
}
