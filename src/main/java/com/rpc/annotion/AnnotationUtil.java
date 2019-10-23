package com.rpc.annotion;

import io.netty.util.internal.StringUtil;

import java.lang.reflect.Method;

/**
 * Created by T440 on 2019/4/15.
 */
public class AnnotationUtil {

    /**
     * 服务端指定服务名
     * @param serviceClass
     * @param <T>
     * @return
     */
    public static <T> String getRpcServiceName(Class<T> serviceClass) {
        RpcService rpcService = serviceClass.getAnnotation(RpcService.class);
        String value = rpcService.value();
        return StringUtil.isNullOrEmpty(value) ? serviceClass.getSimpleName() : value;
    }

    /**
     * 客户端指定服务名
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> String getRpcClientName(Class<T> clazz) {
        RpcClient rpcService = clazz.getAnnotation(RpcClient.class);
        String value = rpcService.value();
        return StringUtil.isNullOrEmpty(value) ? clazz.getSimpleName() : value;
    }

    /**
     * 方法名约束
     * @param method
     * @return
     */
    public static String getRpcMethodName(Method method) {
        RpcMethod rpcMethod = method.getAnnotation(RpcMethod.class);
        String value = rpcMethod.value();
        return StringUtil.isNullOrEmpty(value) ? method.getName() : value;
    }
}
