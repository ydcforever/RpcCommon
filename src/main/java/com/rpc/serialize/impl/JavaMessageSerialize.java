package com.rpc.serialize.impl;

import com.rpc.serialize.MessageSerialize;

import java.io.*;

/**
 * Created by T440 on 2019/4/13.
 */
public class JavaMessageSerialize implements MessageSerialize {
    @Override
    public <T> byte[] serialize(T object) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(object);
        oos.flush();
        oos.close();
        return out.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream inn = new ObjectInputStream(in);
        return (T) inn.readObject();
    }
}
