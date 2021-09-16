package com.itechartgroup.avvinidiktov.PeopleFlow.service;

import com.itechartgroup.avvinidiktov.PeopleFlow.model.dto.EmployeeDTO;

import java.util.Optional;

public interface EmployeeService {
    Optional<EmployeeDTO> addEmployee(EmployeeDTO employeeDTO);
}
