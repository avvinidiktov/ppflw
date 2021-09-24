package com.itechartgroup.avvinidiktov.consumer.impl;

import com.itechartgroup.avvinidiktov.core.model.ChangeStateReq;
import com.itechartgroup.avvinidiktov.consumer.AbstractConsumer;
import com.itechartgroup.avvinidiktov.service.EmployeeStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeStateConsumer implements AbstractConsumer<ChangeStateReq, Boolean> {

    private final EmployeeStateService employeeStateService;

    @KafkaListener(topics = "${kafka.req.topic.state}", groupId = "${kafka.group.id.state}")
    @SendTo()
    public Boolean consume(ChangeStateReq req) {
        return employeeStateService.sendEvent(req);
    }
}
