package com.demo.lookopediaSinarmas.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
