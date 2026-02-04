package com.example.springkafka.producer;

import com.example.springkafka.common.model.Message;
import java.time.Duration;
import java.util.Map;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 2, topics = "spring-kafka-topic")
@ActiveProfiles("test")
class KafkaProducerIT {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private EmbeddedKafkaBroker broker;

    @Test
    void sendsMessageToKafka() {
        Map<String, Object> props = KafkaTestUtils.consumerProps("testGroup", "true", broker);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example.springkafka.common.model");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Message.class.getName());
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        Consumer<String, Message> consumer =
                new DefaultKafkaConsumerFactory<String, Message>(props).createConsumer();

        broker.consumeFromAnEmbeddedTopic(consumer, "spring-kafka-topic");

        kafkaProducer.sendMessage(new Message("hello", "test"));

        ConsumerRecord<String, Message> record =
                KafkaTestUtils.getSingleRecord(consumer, "spring-kafka-topic", Duration.ofSeconds(5));

        assertThat(record.value().getContent()).isEqualTo("hello");
        consumer.close();
    }
}
