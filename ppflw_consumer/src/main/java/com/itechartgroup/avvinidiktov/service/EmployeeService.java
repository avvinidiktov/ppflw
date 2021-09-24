package com.itechartgroup.avvinidiktov.service;

import com.itechartgroup.avvinidiktov.core.model.EmployeeDTO;

import java.util.Optional;

public interface EmployeeService {
    Optional<EmployeeDTO> addEmployee(EmployeeDTO employeeDTO);
}
