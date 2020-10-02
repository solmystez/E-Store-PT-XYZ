package com.demo.lookopediaSinarmas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.demo.lookopediaSinarmas.services.image.ImageStorageProperties;

@SpringBootApplication
@ComponentScan(basePackages = {"com.demo.lookopediaSinarmas"})
@EnableConfigurationProperties({
	ImageStorageProperties.class
})
public class LookopediaSinarmasApplication {
	
	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	public static void main(String[] args) {
		//create folder "upload" at startup if not created
//		new File()
		SpringApplication.run(LookopediaSinarmasApplication.class, args);
	}

}
