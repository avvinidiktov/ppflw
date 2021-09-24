package com.itechartgroup.avvinidiktov.kafka.producer.impl;

import com.itechartgroup.avvinidiktov.core.model.EmployeeDTO;
import com.itechartgroup.avvinidiktov.kafka.producer.AbstractProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeProducer implements AbstractProducer<EmployeeDTO, Optional<EmployeeDTO>> {

    private final ReplyingKafkaTemplate<String, EmployeeDTO, EmployeeDTO> kafkaTemplate;

    @Value("${kafka.request.topic}")
    private String REQ_TOPIC;


    public Optional<EmployeeDTO> sendMessage(EmployeeDTO message) throws ExecutionException, InterruptedException, TimeoutException {
        log.info(String.format("#### -> Producing message -> %s", message));

        ProducerRecord<String, EmployeeDTO> record = new ProducerRecord<>(REQ_TOPIC, null, null, message);
        RequestReplyFuture<String, EmployeeDTO, EmployeeDTO> future = kafkaTemplate.sendAndReceive(record);
        return Optional.of(future.get(10, TimeUnit.SECONDS).value());
    }
}
