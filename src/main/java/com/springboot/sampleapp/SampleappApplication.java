package com.springboot.sampleapp;

import java.sql.Timestamp;
import java.util.Map;
import java.util.HashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SampleappApplication {

	public static void main(final String[] args) {
		SpringApplication.run(SampleappApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") final String name) {
		return String.format("Hello %s!", name);
	}

	@GetMapping("/timestamp")
	public Map<String, Timestamp> timestamp() {

		Map<String, Timestamp> stampMap = new HashMap<String, Timestamp>();
		stampMap.put("timestamp", new Timestamp(System.currentTimeMillis()));		

		return stampMap;
    }
}
