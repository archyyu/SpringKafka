package com.example.springkafka.producer;

import com.example.springkafka.common.model.Message;
import com.example.springkafka.consumer.KafkaConsumer;
import com.example.springkafka.consumer.SpringKafkaConsumerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {
        SpringKafkaProducerApplication.class,
        SpringKafkaConsumerApplication.class
})
@EmbeddedKafka(partitions = 2, topics = "spring-kafka-topic")
@ActiveProfiles("test")
class KafkaEndToEndIT {

    @Autowired
    private KafkaProducer kafkaProducer;

    @SpyBean
    private KafkaConsumer kafkaConsumer;

    @Test
    void producerMessageIsConsumed() {
        kafkaProducer.sendMessage(new Message("e2e", "test"));
        verify(kafkaConsumer, timeout(5000)).listen(any(Message.class));
    }
}
