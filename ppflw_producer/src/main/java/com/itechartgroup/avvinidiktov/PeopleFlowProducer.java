package com.itechartgroup.avvinidiktov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class PeopleFlowProducer {
	public static void main(String[] args) {
		SpringApplication.run(PeopleFlowProducer.class, args);
	}
}
