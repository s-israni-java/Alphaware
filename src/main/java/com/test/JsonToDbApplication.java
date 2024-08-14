package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.test.*")
public class JsonToDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(JsonToDbApplication.class, args);
	}

}
