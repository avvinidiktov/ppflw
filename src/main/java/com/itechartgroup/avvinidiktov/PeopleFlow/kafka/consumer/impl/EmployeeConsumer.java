package com.itechartgroup.avvinidiktov.PeopleFlow.kafka.consumer.impl;


import com.itechartgroup.avvinidiktov.PeopleFlow.kafka.consumer.AbstractConsumer;
import com.itechartgroup.avvinidiktov.PeopleFlow.model.dto.EmployeeDTO;
import com.itechartgroup.avvinidiktov.PeopleFlow.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeConsumer implements AbstractConsumer<EmployeeDTO, EmployeeDTO> {

    private final EmployeeService employeeService;

    @KafkaListener(topics = "${kafka.request.topic}", groupId = "${kafka.group.id.empl}")
    @SendTo()
    public EmployeeDTO consume(EmployeeDTO employeeDTO) {
        Optional<EmployeeDTO> res = employeeService.addEmployee(employeeDTO);

        return res.orElse(null);
    }
}
