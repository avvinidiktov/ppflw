package com.itechartgroup.avvinidiktov.configs;

import com.itechartgroup.avvinidiktov.core.model.ChangeStateReq;
import com.itechartgroup.avvinidiktov.core.model.EmployeeDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

@EnableKafka
@Configuration
public class KafkaConfigs {

    @Value("${kafka.reply.topic}")
    private String topicContainer;
    @Value("${kafka.group.id.empl}")
    private String groupId;

    @Value("${kafka.reply.topic.state}")
    private String topicContainerState;
    @Value("${kafka.group.id.state}")
    private String groupIdState;


    @Bean
    public ReplyingKafkaTemplate<String, EmployeeDTO, EmployeeDTO> replyingKafkaTemplate(
            ProducerFactory<String, EmployeeDTO> producerFactory,
            ConcurrentKafkaListenerContainerFactory<String, EmployeeDTO> containerFactory) {
        ConcurrentMessageListenerContainer<String, EmployeeDTO> replyContainer =
                containerFactory.createContainer(topicContainer);
        replyContainer.getContainerProperties().setGroupId(groupId);
        return new ReplyingKafkaTemplate<>(producerFactory, replyContainer);
    }

    @Bean
    public ReplyingKafkaTemplate<String, ChangeStateReq, Boolean> replyingKafkaTemplateState(
            ProducerFactory<String, ChangeStateReq> producerFactory,
            ConcurrentKafkaListenerContainerFactory<String, Boolean> containerFactory) {
        ConcurrentMessageListenerContainer<String, Boolean> replyContainer =
                containerFactory.createContainer(topicContainerState);
        replyContainer.getContainerProperties().setGroupId(groupIdState);
        return new ReplyingKafkaTemplate<>(producerFactory, replyContainer);
    }


    @Bean
    public KafkaTemplate<String, ChangeStateReq> replyStateTemplate(ProducerFactory<String, ChangeStateReq> pf,
                                                            ConcurrentKafkaListenerContainerFactory<String, Boolean> factory) {
        KafkaTemplate<String, ChangeStateReq> kafkaTemplate = new KafkaTemplate<>(pf);
        factory.setReplyTemplate(kafkaTemplate);
        return kafkaTemplate;
    }

    @Bean
    public KafkaTemplate<String, EmployeeDTO> replyTemplate(ProducerFactory<String, EmployeeDTO> pf,
                                                    ConcurrentKafkaListenerContainerFactory<String, EmployeeDTO> factory) {
        KafkaTemplate<String, EmployeeDTO> kafkaTemplate = new KafkaTemplate<>(pf);
        factory.setReplyTemplate(kafkaTemplate);
        return kafkaTemplate;
    }
}
