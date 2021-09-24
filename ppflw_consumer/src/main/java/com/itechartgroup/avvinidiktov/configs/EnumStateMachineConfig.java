package com.itechartgroup.avvinidiktov.configs;

import com.itechartgroup.avvinidiktov.statemachine.action.ActiveAction;
import com.itechartgroup.avvinidiktov.statemachine.action.AddedAction;
import com.itechartgroup.avvinidiktov.statemachine.action.ApproveAction;
import com.itechartgroup.avvinidiktov.statemachine.action.ErrorAction;
import com.itechartgroup.avvinidiktov.statemachine.action.InCheckAction;
import com.itechartgroup.avvinidiktov.statemachine.event.EmployeeEvent;
import com.itechartgroup.avvinidiktov.statemachine.listener.EmployeeStateListener;
import com.itechartgroup.avvinidiktov.statemachine.state.EmployeeState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class EnumStateMachineConfig extends EnumStateMachineConfigurerAdapter<EmployeeState, EmployeeEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<EmployeeState, EmployeeEvent> states) throws Exception {
        states.withStates()
                .initial(EmployeeState.ADDED)
                .end(EmployeeState.ACTIVE)
                .states(EnumSet.allOf(EmployeeState.class));
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<EmployeeState, EmployeeEvent> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(new EmployeeStateListener());
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<EmployeeState, EmployeeEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(EmployeeState.ADDED)
                .target(EmployeeState.INCHECK)
                .event(EmployeeEvent.PROCESS_EVENT)
                .action(inCheckAction(), errorAction())

                .and()
                .withExternal()
                .source(EmployeeState.INCHECK)
                .target(EmployeeState.APPROVED)
                .event(EmployeeEvent.APPROVE_EVENT)
                .action(approveAction(), errorAction())

                .and()
                .withExternal()
                .source(EmployeeState.APPROVED)
                .target(EmployeeState.ACTIVE)
                .event(EmployeeEvent.ACTIVE_EVENT)
                .action(activeAction(), errorAction());
    }

    @Bean
    public Action<EmployeeState, EmployeeEvent> addAction() {
        return new AddedAction();
    }

    @Bean
    public Action<EmployeeState, EmployeeEvent> inCheckAction() {
        return new InCheckAction();
    }

    @Bean
    public Action<EmployeeState, EmployeeEvent> approveAction() {
        return new ApproveAction();
    }

    @Bean
    public Action<EmployeeState, EmployeeEvent> activeAction() {
        return new ActiveAction();
    }

    @Bean
    public Action<EmployeeState, EmployeeEvent> errorAction() {
        return new ErrorAction();
    }
}