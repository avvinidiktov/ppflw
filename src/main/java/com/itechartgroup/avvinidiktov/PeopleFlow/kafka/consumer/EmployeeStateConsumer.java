package com.itechartgroup.avvinidiktov.PeopleFlow.kafka.consumer;

import com.itechartgroup.avvinidiktov.PeopleFlow.model.dto.ChangeStateReq;
import com.itechartgroup.avvinidiktov.PeopleFlow.service.EmployeeStateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeStateConsumer {

    private final EmployeeStateService employeeStateService;

    @KafkaListener(topics = "${kafka.req.topic.state}", groupId = "${kafka.group.id.state}")
    @SendTo()
    public boolean consume(ChangeStateReq req) {

        return employeeStateService.sendEvent(req);
    }
}
