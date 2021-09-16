package com.itechartgroup.avvinidiktov.PeopleFlow.service.impl;

import com.itechartgroup.avvinidiktov.PeopleFlow.model.dto.ChangeStateReq;
import com.itechartgroup.avvinidiktov.PeopleFlow.service.EmployeeStateService;
import com.itechartgroup.avvinidiktov.PeopleFlow.statemachine.event.EmployeeEvent;
import com.itechartgroup.avvinidiktov.PeopleFlow.statemachine.state.EmployeeState;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

@SpringBootTest
@RunWith(SpringRunner.class)
class EmployeeStateServiceImplTest {

    final Long firstEmployeeId = 1L;
    private final StateMachinePersister<EmployeeState, EmployeeEvent, Long> persister = mock(StateMachinePersister.class);
    @Autowired
    private StateMachineFactory<EmployeeState, EmployeeEvent> factory;
    @Autowired
    private EmployeeStateService sut;

    @Test
    void testStateMachinePersisting() {
        final Long secondEmployeeId = 2L;
        sut.createStatemachineForEmployee(firstEmployeeId);
        sut.createStatemachineForEmployee(firstEmployeeId + 1);
        sut.sendEvent(ChangeStateReq.builder().employeeId(1L).event(EmployeeEvent.PROCESS_EVENT).build());

        Optional<EmployeeState> firstEmployeeState = sut.getStateForEmployee(firstEmployeeId);
        Optional<EmployeeState> secondEmployeeState = sut.getStateForEmployee(secondEmployeeId);

        assertThat(firstEmployeeState.isPresent()).isTrue();
        assertThat(firstEmployeeState.get()).isEqualTo(EmployeeState.INCHECK);
        assertThat(secondEmployeeState.isPresent()).isTrue();
        assertThat(secondEmployeeState.get()).isEqualTo(EmployeeState.ADDED);
    }
}