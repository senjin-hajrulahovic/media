package com.hardcodedlambda.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@RestController
public class MediaApplication {

	@GetMapping("/")
	public String test() {
		return "Hello";
	}

	public static void main(String[] args) {
		SpringApplication.run(MediaApplication.class, args);
	}

}
