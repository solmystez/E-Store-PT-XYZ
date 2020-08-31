package com.demo.lookopediaSinarmas.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.lookopediaSinarmas.services.ImageService;
import com.demo.lookopediaSinarmas.services.otherService.MapValidationErrorService;

@CrossOrigin
@RestController
@RequestMapping("/api/image")
public class ImageController {
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
}
