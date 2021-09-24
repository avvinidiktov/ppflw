package com.itechartgroup.avvinidiktov;

import com.itechartgroup.avvinidiktov.statemachine.event.EmployeeEvent;
import com.itechartgroup.avvinidiktov.statemachine.state.EmployeeState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "")
@ContextConfiguration(initializers = {PeopleFlowConsumerApplicationTest.Initializer.class})
public class PeopleFlowConsumerApplicationTest {

    @Autowired
    private StateMachineFactory<EmployeeState, EmployeeEvent> stateMachineFactory;
    private StateMachine<EmployeeState, EmployeeEvent> stateMachine;

    private static final PostgreSQLContainer<?> sqlContainer;

    static {
        sqlContainer = new PostgreSQLContainer<>("postgres:13.1")
                .withDatabaseName("integration-tests-db")
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

    @Before
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

        stateMachine.sendEvent(processEvent).doOnComplete(() ->
                stateMachine.sendEvent(approveEvent).doOnComplete(() ->
                        stateMachine.sendEvent(activeEvent).subscribe()).subscribe()).subscribe();

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