package com.rpc.transport.impl;

import com.rpc.annotion.AnnotationUtil;
import com.rpc.annotion.RpcMethod;
import com.rpc.message.MessageRequest;
import com.rpc.transport.TransferMethod;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by T440 on 2019/4/14.
 */
public class DefaultTransferMethod implements TransferMethod {

    @Override
    public MessageRequest buildRequest(Method method, Object[] args) {
        MessageRequest messageRequest = new MessageRequest();
        Class<?> clazz = method.getDeclaringClass();
        messageRequest.setServiceName(AnnotationUtil.getRpcClientName(clazz));
        messageRequest.setMethodName(AnnotationUtil.getRpcMethodName(method));
        messageRequest.setParameters(args);
        messageRequest.setParameterTypes(method.getParameterTypes());
        return messageRequest;
    }

    @Override
    public Object handleRequest(Map<String, Object> services, MessageRequest request) throws Throwable {
        String serviceName = request.getServiceName();
        // 根据接口名拿到其实现类对象
        Object service = services.get(serviceName);
        if (service != null) {
            //拿到要调用的方法名、参数类型、参数值
//            String methodName = request.getMethodName();
//            Class<?>[] parameterTypes = request.getParameterTypes();
//            Object[] parameters = request.getParameters();
//            // 拿到接口类对象
//            Class<?> clazz = Class.forName(serviceName);
//            // 拿到实现类对象的指定方法
//            Method method = clazz.getMethod(methodName, parameterTypes);
//            // 通过反射调用方法
//            return method.invoke(service, parameters);
            return methodRouting(service, request.getMethodName(), request.getParameterTypes(), request.getParameters());
        } else {
            throw new Exception(serviceName + " not found.");
        }
    }

    public static Object methodRouting(Object object, String clientRpcMethodName, Class<?>[] parameterTypes, Object[] parameters) {
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(RpcMethod.class)) {
                String serverMethodName = method.getName();
                String serverRpcMethodName = AnnotationUtil.getRpcMethodName(method);
                if (clientRpcMethodName.equals(serverRpcMethodName)) {
                    try {
                        /**
                         * 校验参数类型
                         */
                        Method verifyMethod = clazz.getMethod(serverMethodName, parameterTypes);
                        return verifyMethod.invoke(object, parameters);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
