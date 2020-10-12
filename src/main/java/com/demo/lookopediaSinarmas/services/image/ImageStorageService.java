package com.demo.lookopediaSinarmas.services.image;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.demo.lookopediaSinarmas.exceptions.fileStorage.FileStorageException;
import com.demo.lookopediaSinarmas.exceptions.fileStorage.MyFileNotFoundException;

@Service
public class ImageStorageService {
	//handle image for profile, product, merchant
	
	
	private Path fileStorageLocation = Paths.get("uploads");
	
	@Autowired
	public ImageStorageService(ImageStorageProperties imageStorageProperties) {
		this.fileStorageLocation = Paths.get(imageStorageProperties.getUploadDir()).toAbsolutePath();
//		this.fileStorageLocation = fileStorageLocation.toAbsolutePath();
		
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception e) {
			throw new FileStorageException("Couldn't create the directory to upload");
		}
	}
	
	//function to store file
	public String storeFile(MultipartFile file) {
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
//		if(Files.exists(fileStorageLocation)) {
//			try {
//				Files.createDirectories(fileStorageLocation);
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
		
		try {
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
		
			return fileName;
		} catch (IOException e) {
			throw new FileStorageException("Couldn't store file" + fileName + ", please try again");
		}
	}
	
	public Resource loadFileAsResource(String fileName) {
		
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			
			if(resource.exists()) {
				return resource;
			}else {
				throw new MyFileNotFoundException("File not found " + fileName);
			}
			
		} catch (MalformedURLException e) {
			throw new MyFileNotFoundException("File not found " + fileName);
		}
		
	}
	 
	
//	public  byte[] getImageWithMediaType(String imageName) throws IOException {
//        Path destination =   Paths.get(storageDirectoryPath+"\\"+imageName);// retrieve the image by its name
//        
//        return IOUtils.toByteArray(destination.toUri());
//    }
}
