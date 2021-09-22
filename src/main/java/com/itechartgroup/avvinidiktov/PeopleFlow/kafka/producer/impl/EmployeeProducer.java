package com.itechartgroup.avvinidiktov.PeopleFlow.kafka.producer.impl;

import com.itechartgroup.avvinidiktov.PeopleFlow.kafka.producer.AbstractProducer;
import com.itechartgroup.avvinidiktov.PeopleFlow.model.dto.EmployeeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeProducer implements AbstractProducer<EmployeeDTO, Optional<EmployeeDTO>> {

    private final ReplyingKafkaTemplate<String, EmployeeDTO, EmployeeDTO> requestReplyKafkaTemplate;

    @Value("${kafka.request.topic}")
    private String REQ_TOPIC;


    public Optional<EmployeeDTO> sendMessage(EmployeeDTO message) throws ExecutionException, InterruptedException {
        log.info(String.format("#### -> Producing message -> %s", message));

        ProducerRecord<String, EmployeeDTO> record = new ProducerRecord<>(REQ_TOPIC, null, "employee", message);
        RequestReplyFuture<String, EmployeeDTO, EmployeeDTO> future = requestReplyKafkaTemplate.sendAndReceive(record);
        ConsumerRecord<String, EmployeeDTO> response = future.get();
        return Optional.of(response.value());
    }
}
