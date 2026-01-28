package com.example.springkafka.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SpringKafkaEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaEurekaServerApplication.class, args);
    }

}