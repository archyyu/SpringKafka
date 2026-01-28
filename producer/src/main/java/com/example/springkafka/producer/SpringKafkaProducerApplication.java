package com.example.springkafka.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringKafkaProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaProducerApplication.class, args);
    }

}