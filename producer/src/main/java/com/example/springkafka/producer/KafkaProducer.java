package com.example.springkafka.producer;

import com.example.springkafka.common.model.Message;
import com.example.springkafka.common.model.RedisDistributedLock;


import jakarta.annotation.Resource;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    @Value("${app.kafka.topic.name}")
    private String topicName;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private RedisDistributedLock redisDistributedLock;

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

    @Scheduled(cron = "0 * * * * *")
    public void sendScheduledMessage() {
        boolean result = this.redisDistributedLock.tryLock(topicName, getClass().getName(), Duration.ofSeconds(3));
        if (result) {
            logger.info("get the lock and run the task");
            Message message = new Message("Scheduled message: " + System.currentTimeMillis(), "system");
            sendMessage(message);
        } else {
            logger.warn("failed to acquire the lock");
        }


    }
}