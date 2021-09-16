package com.itechartgroup.avvinidiktov.PeopleFlow.service.impl;

import com.itechartgroup.avvinidiktov.PeopleFlow.model.dto.ChangeStateReq;
import com.itechartgroup.avvinidiktov.PeopleFlow.service.EmployeeStateService;
import com.itechartgroup.avvinidiktov.PeopleFlow.statemachine.event.EmployeeEvent;
import com.itechartgroup.avvinidiktov.PeopleFlow.statemachine.state.EmployeeState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.region.Region;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeStateServiceImpl implements EmployeeStateService {
    final ReadWriteLock locker = new ReentrantReadWriteLock();

    private final StateMachineFactory<EmployeeState, EmployeeEvent> stateMachineFactory;
    private final StateMachinePersister<EmployeeState, EmployeeEvent, Long> persister;

    @Override
    public Optional<EmployeeState> getStateForEmployee(Long employeeId) {
        return Optional.ofNullable(getPersistedStateMachine(employeeId))
                .map(Region::getState)
                .map(State::getId);
    }

    @Override
    public boolean sendEvent(ChangeStateReq req) {
        Mono<Message<EmployeeEvent>> mono = Mono.just(MessageBuilder.withPayload(req.getEvent()).build());
        final StateMachine<EmployeeState, EmployeeEvent> stateMachine = getPersistedStateMachine(req.getEmployeeId());
        final Boolean[] result = {false};
        if (stateMachine != null) {
            stateMachine.sendEvent(mono)
                    .subscribe(res -> {
                        if (res.getResultType().equals(StateMachineEventResult.ResultType.ACCEPTED)) {
                            result[0] = true;
                        }
                    });
            if (result[0]) {
                stateMachine.getExtendedState().getVariables().put("employeeId", req.getEmployeeId());
                saveStateMachineToStorage(req.getEmployeeId(), stateMachine);
            }
        }

        return result[0];
    }

    private StateMachine<EmployeeState, EmployeeEvent> getPersistedStateMachine(Long employeeId) {
        StateMachine<EmployeeState, EmployeeEvent> stateMachine = stateMachineFactory.getStateMachine();
        locker.readLock().lock();
        try {
            stateMachine = persister.restore(stateMachine, employeeId);
            return stateMachine.getId() == null ? null : stateMachine;
        } catch (Exception e) {
            log.error("Unable to restore state machine with uuid ={} and employee id = {}", stateMachine.getUuid(), employeeId);
        } finally {
            locker.readLock().unlock();
        }
        return null;
    }

    @Override
    public void createStatemachineForEmployee(Long employeeId) {
        final StateMachine<EmployeeState, EmployeeEvent> stateMachine = stateMachineFactory.getStateMachine(String.valueOf(employeeId));
        saveStateMachineToStorage(employeeId, stateMachine);
    }

    private void saveStateMachineToStorage(Long employeeId, StateMachine<EmployeeState, EmployeeEvent> stateMachine) {
        locker.writeLock().lock();
        try {
            stateMachine.getExtendedState().getVariables().put("employeeId", employeeId);
            persister.persist(stateMachine, employeeId);
        } catch (Exception e) {
            log.error("Unable to persist state machine with uuid ={} and employee id = {}", stateMachine.getUuid(), employeeId);
        } finally {
            locker.writeLock().unlock();
        }
    }
}
