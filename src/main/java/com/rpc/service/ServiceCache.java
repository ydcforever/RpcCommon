package com.rpc.service;

import com.rpc.annotion.AnnotationUtil;
import com.rpc.annotion.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by T440 on 2019/4/15.
 */
public class ServiceCache implements ApplicationContextAware, InitializingBean {

    private Map<String, Object> services = new ConcurrentHashMap<String, Object>();

    private Class<? extends Annotation> RPC_SERVICE_CLASS = RpcService.class;

    private static ApplicationContext applicationContext = null;

    @Override
    /**
     * 完成service注册
     */
    public void afterPropertiesSet() throws Exception {

    }

    public Map<String, Object> getServices() {
        assertContextInjected();
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RPC_SERVICE_CLASS);
        for(Object service : beans.values()) {
            Class clazz = service.getClass();
            String serviceName = AnnotationUtil.getRpcServiceName(clazz);
            services.putIfAbsent(serviceName, service);
        }
        return services;
    }

    public static <T> T getBean(String name) {
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBean(requiredType);
    }

    public static void clearHolder() {
        applicationContext = null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ServiceCache.applicationContext = applicationContext;
    }

    private static void assertContextInjected() {
        if(applicationContext == null) {
            throw new IllegalStateException("applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder");
        }
    }
}
