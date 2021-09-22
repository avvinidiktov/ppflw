package com.itechartgroup.avvinidiktov.PeopleFlow.kafka.producer.impl;

import com.itechartgroup.avvinidiktov.PeopleFlow.kafka.producer.AbstractProducer;
import com.itechartgroup.avvinidiktov.PeopleFlow.model.dto.ChangeStateReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeStateProducer implements AbstractProducer<ChangeStateReq, Boolean> {
    private final ReplyingKafkaTemplate<String, ChangeStateReq, Boolean> requestReplyKafkaTemplate;

    @Value("${kafka.req.topic.state}")
    private String REQ_TOPIC_SET;


    public Boolean sendMessage(ChangeStateReq req) throws ExecutionException, InterruptedException {
        log.info("#### -> Producing event -> {} for employee id={}", req.getEvent(), req.getEmployeeId());

        ProducerRecord<String, ChangeStateReq> record = new ProducerRecord<>(REQ_TOPIC_SET, null, String.valueOf(req.getEmployeeId()), req);
        RequestReplyFuture<String, ChangeStateReq, Boolean> future = requestReplyKafkaTemplate.sendAndReceive(record);
        ConsumerRecord<String, Boolean> response = future.get();
        return response.value() != null && response.value();
    }
}
