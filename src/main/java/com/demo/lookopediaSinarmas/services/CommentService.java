package com.demo.lookopediaSinarmas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lookopediaSinarmas.domain.Comment;
import com.demo.lookopediaSinarmas.domain.Product;
import com.demo.lookopediaSinarmas.domain.User;
import com.demo.lookopediaSinarmas.exceptions.comment.CommentNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.product.ProductNotFoundException;
import com.demo.lookopediaSinarmas.exceptions.user.UserIdNotFoundException;
import com.demo.lookopediaSinarmas.repositories.CommentRepository;
import com.demo.lookopediaSinarmas.repositories.ProductRepository;
import com.demo.lookopediaSinarmas.repositories.UserRepository;

@Service
public class CommentService {

	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	public Comment postComment(Comment comment, Long product_id, Long user_id) {
		
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
		
		comment.setComment_message(comment.getComment_message());
		comment.setProductComment(product);
		comment.setUserComment(user);
		
		return commentRepository.save(comment);
	}
	
	public List<Comment> removeCommentFromProduct(Long product_id, Long user_id) {
	
		try {
			commentRepository.deleteCommentByUserIdAndProductId(product_id, user_id);
		} catch (Exception e) {
			throw new CommentNotFoundException("No Comment found");
		}
	
//		return commentRepository.findAllByProductId(product_id);
		return null;
	}
	
}
