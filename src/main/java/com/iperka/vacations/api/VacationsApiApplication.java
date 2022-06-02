package com.iperka.vacations.api;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class VacationsApiApplication {

	public static void main(String[] args) {
		log.info("Starting API...");
		new SpringApplicationBuilder(VacationsApiApplication.class)
		.bannerMode(Banner.Mode.CONSOLE)
		.run(args)
		;
	}

}
