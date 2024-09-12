package com.valencia.meal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MealApplication {

	public static void main(String[] args) {
		SpringApplication.run(MealApplication.class, args);
	}

}
