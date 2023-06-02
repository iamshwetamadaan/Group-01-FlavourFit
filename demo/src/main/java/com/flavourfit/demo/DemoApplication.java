package com.flavourfit.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	System.out.println("Test");

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
