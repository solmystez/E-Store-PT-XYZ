package com.demo.lookopediaSinarmas.controller;

import java.security.Principal;
import java.util.List;

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

import com.demo.lookopediaSinarmas.entity.RatingProduct;
import com.demo.lookopediaSinarmas.services.RatingProductService;
import com.demo.lookopediaSinarmas.services.otherService.MapValidationErrorService;

@CrossOrigin
@RestController
@RequestMapping("/api/ratingProduct")
public class RatingProductController {
	
	@Autowired
	private RatingProductService ratingProductService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	@PostMapping("/postRatingProduct/{product_id}/{user_id}")
	public ResponseEntity<?> addRatingProductToProductWithUserId(@Valid @RequestBody RatingProduct rating, BindingResult result,
				@PathVariable Long product_id, @PathVariable Long user_id, Principal principal){

		ResponseEntity<?> mapError = mapValidationErrorService.MapValidationService(result);
		if(mapError != null) return mapError;
		
		RatingProduct rating1 = ratingProductService.postRatingProduct(rating, product_id, user_id, principal.getName());
		return new ResponseEntity<RatingProduct>(rating1, HttpStatus.CREATED);
	}

	
	@DeleteMapping("/deleteRatingProduct/{product_id}/{user_id}")
	public ResponseEntity<?> deleteRatingProductFromProductWithUserId(@PathVariable Long user_id,
			@PathVariable Long product_id) {
		ratingProductService.removeRatingProductFromProduct(product_id, user_id);
		
		return new ResponseEntity<RatingProduct>(HttpStatus.OK);
	}
	
	//getCommentInSpecificProduct
	@GetMapping("/loadRatingProductId/{product_id}")
	public ResponseEntity<?> loadRatingProductInProductId(@PathVariable Long product_id){
		
		List<RatingProduct> rating = ratingProductService.getAllProductRatingInProductId(product_id);

		return new ResponseEntity<List<RatingProduct>>(rating, HttpStatus.OK);
	}
}
