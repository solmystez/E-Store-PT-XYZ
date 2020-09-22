package com.demo.lookopediaSinarmas.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.lookopediaSinarmas.domain.Comment;
import com.demo.lookopediaSinarmas.services.CommentService;
import com.demo.lookopediaSinarmas.services.otherService.MapValidationErrorService;

@CrossOrigin
@RestController
@RequestMapping("/api/comment")
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	@PostMapping("/postComment/{product_id}/{user_id}")
	public ResponseEntity<?> addCommentToProductWithUserId(@Valid @RequestBody Comment comment, BindingResult result,
				@PathVariable Long product_id, @PathVariable Long user_id){

		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		Comment comment1 = commentService.postComment(comment, product_id, user_id);
		return new ResponseEntity<Comment>(comment1, HttpStatus.CREATED);
	}

	
	@DeleteMapping("/deleteComment/{product_id}/{user_id}")
	public ResponseEntity<?> deleteCommentFromProductWithUserId(@PathVariable Long user_id,
			@PathVariable Long product_id) {
	List<Comment> comment1 = commentService.removeCommentFromProduct(product_id, user_id);
	
	return new ResponseEntity<List<Comment>>(comment1, HttpStatus.OK);
}
}
