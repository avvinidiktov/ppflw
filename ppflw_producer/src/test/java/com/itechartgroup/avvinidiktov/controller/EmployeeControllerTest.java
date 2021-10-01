package com.itechartgroup.avvinidiktov.controller;

import com.itechartgroup.avvinidiktov.core.model.EmployeeDTO;
import com.itechartgroup.avvinidiktov.producer.impl.EmployeeProducer;
import com.itechartgroup.avvinidiktov.util.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(controllers = EmployeeController.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmployeeProducer employeeProducer;

    @Test
    public void add_shouldBe201() throws Exception {
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .id(null)
                .name("Test test")
                .age(11)
                .contractDetails("Test details")
                .build();

        EmployeeDTO res = EmployeeDTO.builder()
                .id(1L)
                .name("Test test")
                .age(11)
                .contractDetails("Test details")
                .build();

        given(employeeProducer.sendMessage(employeeDTO)).willReturn(Optional.of(res));

        mvc.perform(post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJson(employeeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(res.getId()))
                .andExpect(jsonPath("$.name").value(res.getName()))
                .andExpect(jsonPath("$.age").value(res.getAge()))
                .andExpect(jsonPath("$.contractDetails").value(res.getContractDetails()));
    }

    @Test
    public void add_shouldBe400() throws Exception {
        EmployeeDTO employeeDTO = EmployeeDTO.builder().build();

        given(employeeProducer.sendMessage(employeeDTO)).willReturn(Optional.empty());

        mvc.perform(post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJson(employeeDTO)))
                .andExpect(status().isBadRequest());
    }
}