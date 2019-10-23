package com.rpc.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.rpc.serialize.MessageSerialize;

/**
 * Created by T440 on 2019/4/16.
 */
public class FastJsonSerialize implements MessageSerialize {
    @Override
    public <T> byte[] serialize(T object) throws Exception {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws Exception {
        return JSON.parseObject(data, clazz);
    }
}
