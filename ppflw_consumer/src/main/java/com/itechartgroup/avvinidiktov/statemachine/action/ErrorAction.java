package com.itechartgroup.avvinidiktov.statemachine.action;

import com.itechartgroup.avvinidiktov.statemachine.event.EmployeeEvent;
import com.itechartgroup.avvinidiktov.statemachine.state.EmployeeState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

@Slf4j
public class ErrorAction implements Action<EmployeeState, EmployeeEvent> {
    @Override
    public void execute(StateContext<EmployeeState, EmployeeEvent> stateContext) {
        log.info("Unable to change status to: {}", stateContext.getTarget().getId());
    }
}
