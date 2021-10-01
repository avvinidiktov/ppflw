package com.itechartgroup.avvinidiktov.producer.impl;

import com.itechartgroup.avvinidiktov.core.model.ChangeStateReq;
import com.itechartgroup.avvinidiktov.producer.AbstractProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeStateProducer implements AbstractProducer<ChangeStateReq, Boolean> {
    private final ReplyingKafkaTemplate<String, ChangeStateReq, Boolean> kafkaTemplate;

    @Value("${kafka.req.topic.state}")
    private String REQ_TOPIC_SET;


    public Boolean sendMessage(ChangeStateReq req) throws ExecutionException, InterruptedException, TimeoutException {
        log.info("#### -> Producing event -> {} for employee id={}", req.getEvent(), req.getEmployeeId());

        ProducerRecord<String, ChangeStateReq> record = new ProducerRecord<>(REQ_TOPIC_SET, null, "state", req);
        RequestReplyFuture<String, ChangeStateReq, Boolean> future = kafkaTemplate.sendAndReceive(record);
        boolean res = future.get(10, TimeUnit.SECONDS).value();
        if (res) {
            log.info("New status={} applied for userId={}", req.getEvent(), req.getEmployeeId());
        } else {
            log.info("Event={} not accepted for userId={}", req.getEvent(), req.getEmployeeId());
        }
        return res;
    }
}
