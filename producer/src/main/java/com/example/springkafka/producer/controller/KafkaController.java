package com.example.springkafka.producer.controller;

import com.example.springkafka.common.model.Message;
import com.example.springkafka.producer.KafkaProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    private final KafkaProducer kafkaProducer;

    public KafkaController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @GetMapping("/send")
    public String sendMessage(@RequestParam String content, @RequestParam(defaultValue = "user") String sender) {
        Message message = new Message(content, sender);
        kafkaProducer.sendMessage(message);
        return "Message sent to Kafka: " + message;
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "Kafka Spring Boot Producer Application is running!";
    }
}