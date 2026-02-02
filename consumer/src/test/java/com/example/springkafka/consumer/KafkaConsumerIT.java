package com.example.springkafka.consumer;

import com.example.springkafka.common.model.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = "spring-kafka-topic")
@ActiveProfiles("test")
class KafkaConsumerIT {

    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    @SpyBean
    private KafkaConsumer kafkaConsumer;

    @Test
    void listenerReceivesMessage() {
        kafkaTemplate.send("spring-kafka-topic", new Message("hi", "test"));
        verify(kafkaConsumer, timeout(5000)).listen(any(Message.class));
    }
}
