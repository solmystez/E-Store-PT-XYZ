package com.demo.lookopediaSinarmas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.demo.lookopediaSinarmas"})
public class LookopediaSinarmasApplication {

	public static void main(String[] args) {
		SpringApplication.run(LookopediaSinarmasApplication.class, args);
	}

}
