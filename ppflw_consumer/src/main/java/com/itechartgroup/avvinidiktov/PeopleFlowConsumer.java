package com.itechartgroup.avvinidiktov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
@EntityScan(basePackages = "com.itechartgroup.avvinidiktov.entity")
public class PeopleFlowConsumer {
	public static void main(String[] args) {
		SpringApplication.run(PeopleFlowConsumer.class, args);
	}
}
