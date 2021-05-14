package com.iperka.vacations.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VacationsApiApplication {

	private static final Logger logger = LoggerFactory.getLogger(VacationsApiApplication.class);

	public static void main(String[] args) {
		logger.info("Starting API...");
		SpringApplication.run(VacationsApiApplication.class, args);
	}

}
