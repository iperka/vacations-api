package com.iperka.vacations.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class VacationsApiApplication {

	public static void main(String[] args) {
		log.info("Starting API...");
		SpringApplication.run(VacationsApiApplication.class, args);
	}

}
