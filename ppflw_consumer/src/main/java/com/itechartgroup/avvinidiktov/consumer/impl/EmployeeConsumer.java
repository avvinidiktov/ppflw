package com.itechartgroup.avvinidiktov.consumer.impl;


import com.itechartgroup.avvinidiktov.core.model.EmployeeDTO;
import com.itechartgroup.avvinidiktov.consumer.AbstractConsumer;
import com.itechartgroup.avvinidiktov.service.EmployeeService;
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
