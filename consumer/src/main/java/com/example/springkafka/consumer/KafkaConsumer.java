package com.example.springkafka.consumer;

import com.example.springkafka.common.model.Message;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "${app.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(Message message) {
        System.out.println("Consumer 1 - Received message: " + message);
    }
}