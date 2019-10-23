package com.rpc.serialize;

/**
 * Created by T440 on 2019/4/12.
 */
public interface MessageSerialize {

    int MESSAGE_LENGTH = 4;

    /**
     * 序列化
     * @param object
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> byte[] serialize(T object) throws Exception;

    /**
     * 反序列化
     * @param data
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    <T> T deserialize(byte[] data, Class<T> clazz) throws Exception;
}
