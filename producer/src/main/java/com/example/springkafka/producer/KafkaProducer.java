package com.example.springkafka.producer;

import com.example.springkafka.common.model.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    @Value("${app.kafka.topic.name}")
    private String topicName;

    private final KafkaTemplate<String, Message> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Message> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private int counter = 0;
    private final int totalPartitions = 2;

    public void sendMessage(Message message) {
        int partition = counter++ % totalPartitions;
        kafkaTemplate.send(topicName, partition, null, message);
        System.out.println("Message sent to Partition [" + partition + "]: " + message);
    }

    @Scheduled(fixedRate = 5000)
    public void sendScheduledMessage() {
        Message message = new Message("Scheduled message: " + System.currentTimeMillis(), "system");
        sendMessage(message);
    }
}