package com.itechartgroup.avvinidiktov.PeopleFlow.service.impl;

import com.itechartgroup.avvinidiktov.PeopleFlow.model.dto.EmployeeDTO;
import com.itechartgroup.avvinidiktov.PeopleFlow.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EmployeeServiceImplTest {
    @Autowired
    private EmployeeService sut;

    private EmployeeDTO testEmployeeDTO;

    @Before
    public void setUp() {
        this.testEmployeeDTO = EmployeeDTO.builder()
                .age(28)
                .name("Test Employee")
                .contractDetails("Nothing interesting")
                .build();
    }

    @Test
    public void addEmployeeTest() {
        Optional<EmployeeDTO> result = sut.addEmployee(testEmployeeDTO);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isNotNull();
    }
}