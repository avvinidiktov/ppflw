package com.itechartgroup.avvinidiktov.PeopleFlow.configs;

import com.itechartgroup.avvinidiktov.PeopleFlow.model.dto.ChangeStateReq;
import com.itechartgroup.avvinidiktov.PeopleFlow.model.dto.EmployeeDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

@Configuration
public class KafkaConfig {
    @Value("${kafka.group.id.empl}")
    private String groupIdEmpl;
    @Value("${kafka.group.id.state}")
    private String groupIdState;
    @Value("${kafka.reply.topic}")
    private String replyTopic;
    @Value("${kafka.reply.topic.state}")
    private String replyTopicState;

    @Bean
    public ReplyingKafkaTemplate<String, EmployeeDTO, EmployeeDTO> replyingKafkaTemplate(ProducerFactory<String, EmployeeDTO> pf,
                                                                                         ConcurrentKafkaListenerContainerFactory<String, EmployeeDTO> factory) {
        ConcurrentMessageListenerContainer<String, EmployeeDTO> replyContainer = factory.createContainer(replyTopic);
        replyContainer.getContainerProperties().setMissingTopicsFatal(false);
        replyContainer.getContainerProperties().setGroupId(groupIdEmpl);
        return new ReplyingKafkaTemplate<>(pf, replyContainer);
    }

    @Bean
    public ReplyingKafkaTemplate<String, ChangeStateReq, Boolean> replyingKafkaTemplateState(ProducerFactory<String, ChangeStateReq> pf,
                                                                                             ConcurrentKafkaListenerContainerFactory<String, Boolean> factory) {
        ConcurrentMessageListenerContainer<String, Boolean> replyContainer = factory.createContainer(replyTopicState);
        replyContainer.getContainerProperties().setMissingTopicsFatal(false);
        replyContainer.getContainerProperties().setGroupId(groupIdState);
        return new ReplyingKafkaTemplate<>(pf, replyContainer);
    }

    @Bean
    public KafkaTemplate<String, EmployeeDTO> replyTemplate(ProducerFactory<String, EmployeeDTO> pf,
                                                            ConcurrentKafkaListenerContainerFactory<String, EmployeeDTO> factory) {
        KafkaTemplate<String, EmployeeDTO> kafkaTemplate = new KafkaTemplate<>(pf);
        factory.getContainerProperties().setMissingTopicsFatal(false);
        factory.setReplyTemplate(kafkaTemplate);
        return kafkaTemplate;
    }

    @Bean
    public KafkaTemplate<String, ChangeStateReq> replyTemplateState(ProducerFactory<String, ChangeStateReq> pf,
                                                                    ConcurrentKafkaListenerContainerFactory<String, ChangeStateReq> factory) {
        KafkaTemplate<String, ChangeStateReq> kafkaTemplate = new KafkaTemplate<>(pf);
        factory.getContainerProperties().setMissingTopicsFatal(false);
        factory.setReplyTemplate(kafkaTemplate);
        return kafkaTemplate;
    }
}
