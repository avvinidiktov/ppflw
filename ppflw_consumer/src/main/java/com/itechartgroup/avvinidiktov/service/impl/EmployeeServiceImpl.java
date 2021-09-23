package com.itechartgroup.avvinidiktov.service.impl;

import com.itechartgroup.avvinidiktov.core.model.EmployeeDTO;
import com.itechartgroup.avvinidiktov.entity.Employee;
import com.itechartgroup.avvinidiktov.repository.EmployeeRepository;
import com.itechartgroup.avvinidiktov.service.EmployeeService;
import com.itechartgroup.avvinidiktov.service.EmployeeStateService;
import com.itechartgroup.avvinidiktov.util.EmployeeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeStateService employeeStateService;


    @Override
    @Transactional
    public Optional<EmployeeDTO> addEmployee(EmployeeDTO employeeDTO) {
        final Optional<Employee> savedEmployee = saveEmployee(employeeDTO);
        savedEmployee
                .map(Employee::getId)
                .ifPresent(employeeStateService::createStatemachineForEmployee);

        return savedEmployee.map(EmployeeUtils::mapEmployeeDTO);
    }

    private Optional<Employee> saveEmployee(EmployeeDTO employeeDTO) {
        Employee employeeToSave = EmployeeUtils.mapEmployee(employeeDTO);
        log.info("Trying to save employee {}", employeeToSave);
        return Optional.of(employeeRepository.save(employeeToSave));
    }
}
