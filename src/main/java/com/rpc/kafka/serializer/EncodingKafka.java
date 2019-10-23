package com.rpc.kafka.serializer;

import com.rpc.serialize.MessageSerialize;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * Created by T440 on 2019/3/4.
 */
public class EncodingKafka implements Serializer<Object> {

    private MessageSerialize messageSerialize;

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, Object o) {
        byte[] write = null;
        try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(byteArray)){
            outputStream.writeObject(o);
            outputStream.flush();
            write = byteArray.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return write;
    }

    @Override
    public void close() {

    }
}
