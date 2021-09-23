package com.itechartgroup.avvinidiktov.configs;

import com.itechartgroup.avvinidiktov.statemachine.event.EmployeeEvent;
import com.itechartgroup.avvinidiktov.statemachine.state.EmployeeState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;


@Configuration
public class PersistConfig {
    @Bean
    public StateMachinePersister<EmployeeState, EmployeeEvent, Long> persister(
            StateMachinePersist<EmployeeState, EmployeeEvent, Long> defaultPersist) {
        return new DefaultStateMachinePersister<>(defaultPersist);
    }

    @Bean
    public StateMachineRuntimePersister<EmployeeState, EmployeeEvent, Long> stateMachineRuntimePersister(
            JpaStateMachineRepository jpaStateMachineRepository) {
        return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
    }
}
