package com.itechartgroup.avvinidiktov.util;

import com.itechartgroup.avvinidiktov.core.model.EmployeeDTO;
import com.itechartgroup.avvinidiktov.entity.Employee;

public final class EmployeeUtils {

    public static EmployeeDTO mapEmployeeDTO(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .contractDetails(employee.getContractDetails())
                .age(employee.getAge())
                .build();
    }

    public static Employee mapEmployee(EmployeeDTO employeeDTO) {
        return Employee.builder()
                .id(employeeDTO.getId())
                .name(employeeDTO.getName())
                .contractDetails(employeeDTO.getContractDetails())
                .age(employeeDTO.getAge())
                .build();
    }
}
