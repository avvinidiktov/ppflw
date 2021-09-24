package com.itechartgroup.avvinidiktov.service.impl;

import com.itechartgroup.avvinidiktov.core.model.EmployeeDTO;
import com.itechartgroup.avvinidiktov.entity.Employee;
import com.itechartgroup.avvinidiktov.repository.EmployeeRepository;
import com.itechartgroup.avvinidiktov.service.EmployeeStateService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeStateService employeeStateService;
    @InjectMocks
    private EmployeeServiceImpl sut;

    @Test
    public void addEmployee() {
        EmployeeDTO inputDTO = EmployeeDTO.builder()
                .id(null)
                .name("Test Test")
                .age(12)
                .contractDetails("None")
                .build();

        Employee outputEntity = Employee.builder()
                .id(1L)
                .name("Test Test")
                .age(12)
                .contractDetails("None")
                .build();

        Mockito.when(employeeRepository.save(any())).thenReturn(outputEntity);
        Mockito.doNothing().when(employeeStateService).createStatemachineForEmployee(outputEntity.getId());

        EmployeeDTO employeeDTO = sut.addEmployee(inputDTO).get();

        Mockito.verify(employeeStateService).createStatemachineForEmployee(outputEntity.getId());
        assertThat(employeeDTO).isNotNull();
    }

    @Test
    public void addEmployee_notExecutedCreatingStateMachine() {
        EmployeeDTO inputDTO = EmployeeDTO.builder()
                .id(null)
                .name("Test Test")
                .age(12)
                .contractDetails("None")
                .build();

        Employee outputEntity = Employee.builder()
                .id(1L)
                .name("Test Test")
                .age(12)
                .contractDetails("None")
                .build();

        Mockito.verify(employeeStateService, Mockito.never()).createStatemachineForEmployee(outputEntity.getId());
    }
}