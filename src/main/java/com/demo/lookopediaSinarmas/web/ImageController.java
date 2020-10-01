package com.demo.lookopediaSinarmas.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	
	
	private static String UPLOAD_DIR = "uploads";
	
	@PostMapping("/upload")
	public String upload(@RequestParam("file") MultipartFile file,
			HttpServletRequest request) {
		try {
			String fileName = file.getOriginalFilename();
			String path = request.getServletContext().getRealPath("")
					+ UPLOAD_DIR + File.separator + fileName;
			saveFile(file.getInputStream(), path);
			return fileName;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void saveFile(InputStream inputStream, String path) {
		try {
			OutputStream outputStream = new FileOutputStream(new File(path));
			int read = 0;
			byte[] bytes = new byte[1024];
			while((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	//method2
	@PostMapping("/testUpload")
	public @ResponseBody String uploadImage2(@RequestParam("imageValue") String imageValue,HttpServletRequest request)
    {
        try
        {
            //This will decode the String which is encoded by using Base64 class
            byte[] imageByte = Base64.decodeBase64(imageValue);

            String directory = request.getRealPath("/")
            		+ new Date().getTime() 
            		+"-"+ (10000 + new Random().nextInt(100000))
            		+"-"+"image.png";

            new FileOutputStream(directory).write(imageByte);
            return "success ";
        }
        catch(Exception e)
        {
            return "error = "+e;
        }

    }
}
