package com.rpc.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * Created by T440 on 2019/3/4.
 */
public interface ConsumerHandler<K, T> {
    void handler(ConsumerRecord<K, T> record);
}
