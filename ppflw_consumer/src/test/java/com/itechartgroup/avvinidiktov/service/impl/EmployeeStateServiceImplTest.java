package com.itechartgroup.avvinidiktov.service.impl;


import com.itechartgroup.avvinidiktov.core.model.ChangeStateReq;
import com.itechartgroup.avvinidiktov.statemachine.event.EmployeeEvent;
import com.itechartgroup.avvinidiktov.statemachine.state.EmployeeState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "integration")
@ContextConfiguration(initializers = {EmployeeStateServiceImplTest.Initializer.class})
public class EmployeeStateServiceImplTest {

    private static final PostgreSQLContainer<?> sqlContainer;

    static {
        sqlContainer = new PostgreSQLContainer<>("postgres:13.1")
                .withDatabaseName("statemachine-tests-db")
                .withUsername("sa")
                .withPassword("sa");
        sqlContainer.start();
    }


    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + sqlContainer.getJdbcUrl(),
                    "spring.datasource.username=" + sqlContainer.getUsername(),
                    "spring.datasource.password=" + sqlContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired
    private EmployeeStateServiceImpl sut;

    @Test
    public void testMachinePersistingCheck() {
        final Long firstEmployeeId = 1L;
        final Long secondEmployeeId = 2L;

        final ChangeStateReq firstEmlStateChangeReq = ChangeStateReq.builder()
                .employeeId(firstEmployeeId)
                .event(EmployeeEvent.PROCESS_EVENT.name())
                .build();

        final ChangeStateReq secondEmlStateChangeReq = ChangeStateReq.builder()
                .employeeId(firstEmployeeId)
                .event(EmployeeEvent.ACTIVE_EVENT.name())
                .build();

        sut.createStatemachineForEmployee(firstEmployeeId);
        sut.createStatemachineForEmployee(secondEmployeeId);
        assertThat(sut.sendEvent(firstEmlStateChangeReq)).isTrue();
        assertThat(sut.sendEvent(secondEmlStateChangeReq)).isFalse();

        Optional<EmployeeState> firstEmployeeState = sut.getStateForEmployee(firstEmployeeId);
        Optional<EmployeeState> secondEmployeeState = sut.getStateForEmployee(secondEmployeeId);

        assertThat(firstEmployeeState.isPresent()).isTrue();
        assertThat(secondEmployeeState.isPresent()).isTrue();
        assertThat(firstEmployeeState.get()).isEqualTo(EmployeeState.INCHECK);
        assertThat(secondEmployeeState.get()).isEqualTo(EmployeeState.ADDED);
    }
}