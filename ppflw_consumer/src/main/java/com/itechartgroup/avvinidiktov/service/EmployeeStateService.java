package com.itechartgroup.avvinidiktov.service;

import com.itechartgroup.avvinidiktov.core.model.ChangeStateReq;
import com.itechartgroup.avvinidiktov.statemachine.state.EmployeeState;

import java.util.Optional;

/**
 * State machine managing service
 */
public interface EmployeeStateService {
    /**
     * Send new event, return accepted or not
     *
     * @param req dto payload with employeeId and event name
     * @return Return current state
     */
    boolean sendEvent(ChangeStateReq req);

    /**
     * Return saved state for employee
     *
     * @param employeeId employee id
     * @return Return current state
     */
    Optional<EmployeeState> getStateForEmployee(Long employeeId);

    /**
     * Create new statemachine for employee
     *
     * @param employeeId employee id
     */
    void createStatemachineForEmployee(Long employeeId);
}
