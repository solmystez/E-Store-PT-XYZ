package com.demo.lookopediaSinarmas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.lookopediaSinarmas.services.image.ImageStorageService;
import com.demo.lookopediaSinarmas.services.otherService.MapValidationErrorService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/image")
public class ImageController {
	
	@Autowired
	private ImageStorageService imageService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
}
