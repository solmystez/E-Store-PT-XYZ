package com.demo.lookopediaSinarmas.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.lookopediaSinarmas.repositories.UserRepository;
import com.demo.lookopediaSinarmas.web.UserController;

@Service
public class ImageService {
	//handle image for profile, product, merchant
	private static Logger log = LoggerFactory.getLogger(UserController.class);
	public static String uploadDirectory = System.getProperty("user.dir") + "/uploads";

	@Autowired
	UserRepository userRepository;
	
	//upload image to base64 string
	public static void decodeImage(String imageVal) throws Exception {
					
			
//			byte[] imageByte = imageStream.readAllBytes();
//			String imageString = Base64.getEncoder().encodeToString(imageByte);
			
			
			//save file local
//			String filePath = Paths.get(uploadDirectory).toString();
//			FileWriter fileWriter = new FileWriter(filePath);
//			fileWriter.write(imageString);
//			fileWriter.close();
//			imageStream.close();
			
		
		
		
		
		
//			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
//			stream.write(file.getBytes());
//			stream.close();
			
	}
	
	//method 2
	public static void decoder(String base64Image, String pathFile) {
	      try (FileOutputStream imageOutFile = new FileOutputStream(pathFile)) {
	          // Converting a Base64 String into Image byte array
	          byte[] imageByteArray = Base64.getDecoder().decode(base64Image);
	          imageOutFile.write(imageByteArray);
	      } catch (FileNotFoundException e) {
	          System.out.println("Image not found" + e);
	      } catch (IOException ioe) {
	          System.out.println("Exception while reading the Image " + ioe);
	      }
	  }
	
}
