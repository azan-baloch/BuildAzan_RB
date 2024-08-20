package com.buildazan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BuildAzanApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuildAzanApplication.class, args);
	}

}
