package com.itechartgroup.avvinidiktov.PeopleFlow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.itechartgroup.avvinidiktov.PeopleFlow.model.entity")
public class PeopleFlowApplication {
	public static void main(String[] args) {
		SpringApplication.run(PeopleFlowApplication.class, args);
	}
}
