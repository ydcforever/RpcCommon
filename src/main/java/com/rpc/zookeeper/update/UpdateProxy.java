package com.rpc.zookeeper.update;

import org.apache.curator.framework.CuratorFramework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by ydc on 2019/5/7.
 */
public class UpdateProxy implements InvocationHandler {

    private CuratorFramework curator;

    private String basePath;

    private String serviceName;

    private KeepConnectionUpdate keepConnectionUpdate;

    public UpdateProxy(CuratorFramework curator, String basePath, String serviceName, KeepConnectionUpdate keepConnectionUpdate) {
        this.curator = curator;
        this.basePath = basePath;
        this.serviceName = serviceName;
        this.keepConnectionUpdate = keepConnectionUpdate;
    }

    public KeepConnectionUpdate getProxy() {
        return (KeepConnectionUpdate) Proxy.newProxyInstance(KeepConnectionUpdate.class.getClassLoader(),
                new Class<?>[]{KeepConnectionUpdate.class}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            byte[] data = curator.getData().watched().forPath(basePath + "/" + serviceName);
            Integer cnt = Integer.parseInt(new String(data));
            keepConnectionUpdate.setCnt(cnt);
            method.invoke(keepConnectionUpdate,args);
            curator.setData().forPath(basePath + "/" + serviceName, cnt.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keepConnectionUpdate;
    }
}
