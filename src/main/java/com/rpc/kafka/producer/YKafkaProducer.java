package com.rpc.kafka.producer;

import com.rpc.kafka.serializer.EncodingKafka;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.List;
import java.util.Properties;


/**
 * Created by T440 on 2019/2/19.
 */
public class YKafkaProducer<T> extends Thread {

    private Properties properties;

    private Producer<String, T> producer;

    private String topic;

    public YKafkaProducer(String topic) {
        this.topic = topic;
    }

    public YKafkaProducer<T> properties(Properties properties) {
        this.properties = properties;
        return this;
    }

    public YKafkaProducer<T> build() {
        if (this.properties == null) {
            this.properties = new Properties();
            this.properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
            this.properties.put(ProducerConfig.ACKS_CONFIG, "all");
            this.properties.put(ProducerConfig.RETRIES_CONFIG, 0);
            this.properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
            this.properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
            this.properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
            this.properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            this.properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, EncodingKafka.class);
        }
        this.producer = new KafkaProducer<String, T>(this.properties);
        return this;
    }

    public void send(List<T> list) {
        for (T t : list) {
            ProducerRecord<String, T> producerRecord = new ProducerRecord<String, T>(this.topic, t);
            sendRecord(producerRecord);
        }
        close();
    }

    private void sendRecord(ProducerRecord<String, T> producerRecord) {
        this.producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (recordMetadata != null) {
                    System.out.println("  发送成功：" + " offset: " +
                            recordMetadata.offset() + " partition: " + recordMetadata.partition() + " topic: " + recordMetadata.topic());
                }
                if (e != null) {
                    System.out.println("异常：" + e.getMessage());
                }
            }
        });
    }

    public void close() {
        this.producer.close();
    }
}
