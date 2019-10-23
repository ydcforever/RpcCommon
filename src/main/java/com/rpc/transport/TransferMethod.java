package com.rpc.transport;

import com.rpc.message.MessageRequest;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by T440 on 2019/4/14.
 */
public interface TransferMethod {

    /**
     * 构建 request
     * @param method
     * @param args
     * @return
     */
    MessageRequest buildRequest(Method method, Object[] args);

    /**
     * 处理request,即映射method
     * @param services
     * @param request
     * @return
     * @throws Throwable
     */
    Object handleRequest(Map<String, Object> services, MessageRequest request) throws Throwable;
}
