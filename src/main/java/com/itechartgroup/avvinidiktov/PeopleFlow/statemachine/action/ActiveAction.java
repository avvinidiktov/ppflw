package com.itechartgroup.avvinidiktov.PeopleFlow.statemachine.action;

import com.itechartgroup.avvinidiktov.PeopleFlow.statemachine.event.EmployeeEvent;
import com.itechartgroup.avvinidiktov.PeopleFlow.statemachine.state.EmployeeState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

@Slf4j
public class ActiveAction implements Action<EmployeeState, EmployeeEvent> {

    @Override
    public void execute(StateContext<EmployeeState, EmployeeEvent> stateContext) {
        final Long employeeId = stateContext.getExtendedState().get("employeeId", Long.class);
        log.info("Employee with id={} in state {}", employeeId, stateContext.getEvent());
    }
}
