package com.itechartgroup.avvinidiktov.PeopleFlow;

import com.itechartgroup.avvinidiktov.PeopleFlow.statemachine.event.EmployeeEvent;
import com.itechartgroup.avvinidiktov.PeopleFlow.statemachine.state.EmployeeState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.config.StateMachineFactory;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PeopleFlowApplicationTests {
    @Autowired
    private StateMachineFactory<EmployeeState, EmployeeEvent> stateMachineFactory;
    private StateMachine<EmployeeState, EmployeeEvent> stateMachine;

    @BeforeEach
    public void setUp() {
        stateMachine = stateMachineFactory.getStateMachine();
    }

    @Test
    public void contextLoads() {
        assertThat(stateMachine).isNotNull();
        assertThat(stateMachine.getState().getId()).isEqualTo(EmployeeState.ADDED);
    }

    @Test
    public void testStateMachineFlow() {
        Mono<Message<EmployeeEvent>> processEvent = Mono.just(MessageBuilder.withPayload(EmployeeEvent.PROCESS_EVENT).build());
        Mono<Message<EmployeeEvent>> approveEvent = Mono.just(MessageBuilder.withPayload(EmployeeEvent.APPROVE_EVENT).build());
        Mono<Message<EmployeeEvent>> activeEvent = Mono.just(MessageBuilder.withPayload(EmployeeEvent.ACTIVE_EVENT).build());

        stateMachine.sendEvent(processEvent).doOnComplete(() -> {
            stateMachine.sendEvent(approveEvent).doOnComplete(() -> {
                stateMachine.sendEvent(activeEvent).subscribe();
            }).subscribe();
        }).subscribe();

        assertThat(stateMachine.getState().getId()).isEqualTo(EmployeeState.ACTIVE);
    }

    @Test
    public void testNotAllowedState() {
        Mono<Message<EmployeeEvent>> approveEvent = Mono.just(MessageBuilder.withPayload(EmployeeEvent.APPROVE_EVENT).build());

        final Boolean[] result = {false};
        stateMachine.sendEvent(approveEvent).subscribe(res -> {
            if (!res.getResultType().equals(StateMachineEventResult.ResultType.ACCEPTED)) {
                result[0] = true;
            }
        });

        assertThat(result[0]).isTrue();
    }

}
