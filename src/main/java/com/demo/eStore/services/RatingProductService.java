package com.demo.eStore.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.eStore.entity.Cart;
import com.demo.eStore.entity.Orders;
import com.demo.eStore.entity.Product;
import com.demo.eStore.entity.RatingProduct;
import com.demo.eStore.entity.User;
import com.demo.eStore.exceptions.comment.CommentNotFoundException;
import com.demo.eStore.exceptions.product.ProductNotFoundException;
import com.demo.eStore.exceptions.user.UserIdNotFoundException;
import com.demo.eStore.repositories.CartRepository;
import com.demo.eStore.repositories.OrderRepository;
import com.demo.eStore.repositories.ProductRepository;
import com.demo.eStore.repositories.RatingProductRepository;
import com.demo.eStore.repositories.UserRepository;

@Service
public class RatingProductService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	CartRepository cartRepository;
	
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
		
		//tarik order, tarik produknya, tarik cart by status  
		
//		Orders order = orderRepository.findById(order_id).get();
//		order.setHasRating("Done");
//		orderRepository.save(order);
		
		String status = "Paid";
		//find cart by order_id and product_id
		//set hasRating = done
		List<Cart> carts = cartRepository.findByOrderIdProductIdAndStatus(order_id, product_id, status);
		
		for(int i=0; i<carts.size(); i++) {
			carts.get(i).setHasRating("Done");
		}
		cartRepository.saveAll(carts);
		
		
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
