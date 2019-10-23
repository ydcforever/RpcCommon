package com.rpc.kafka.consumer;

import com.rpc.kafka.serializer.DecodingKafka;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by T440 on 2019/2/19.
 */
public class YKafkaConsumer<K, T> extends Thread {

    private Properties properties;

    private String topic;

    private ConsumerHandler<K, T> consumerHandler;

    public YKafkaConsumer(String topic, ConsumerHandler<K, T> consumerHandler) {
        this.topic = topic;
        this.consumerHandler = consumerHandler;
    }

    public YKafkaConsumer<K, T> properties(Properties properties) {
        this.properties = properties;
        return this;
    }

    public YKafkaConsumer<K, T> build() {
        if (this.properties == null) {
            this.properties = new Properties();
            this.properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
            this.properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
            this.properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
            this.properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
            this.properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            this.properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, DecodingKafka.class);
        }
        return this;
    }

    @Override
    public void run() {
        KafkaConsumer<K, T> kafkaConsumer = new KafkaConsumer<K, T>(this.properties);
        kafkaConsumer.subscribe(Arrays.asList(this.topic));
        while (true) {
            ConsumerRecords<K, T> records = kafkaConsumer.poll(Duration.ofSeconds(10));
            for (ConsumerRecord<K, T> record : records) {
                consumerHandler.handler(record);
            }
        }
    }
}
