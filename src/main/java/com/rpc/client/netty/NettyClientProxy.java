package com.rpc.client.netty;

import com.rpc.annotion.AnnotationUtil;
import com.rpc.message.MessageRequest;
import com.rpc.message.MessageResponse;
import com.rpc.net.ServiceAddress;
import com.rpc.serialize.MessageSerialize;
import com.rpc.serialize.impl.ProtoStuffMessageSerialize;
import com.rpc.service.balance.policy.BalancePolicy;
import com.rpc.service.balance.policy.impl.RandomBalancePolicy;
import com.rpc.service.management.ServiceProvider;
import com.rpc.transport.TransferMethod;
import com.rpc.transport.impl.DefaultTransferMethod;
import io.netty.util.internal.StringUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by T440 on 2019/4/14.
 */
public class NettyClientProxy implements InvocationHandler {

    private static final TransferMethod transferMethod = new DefaultTransferMethod();
    private MessageSerialize messageSerialize;
    private ServiceAddress serviceAddress;
    private Class<?> serviceClass;
    private String serviceName;
    private ServiceProvider serviceProvider;
    private BalancePolicy balancePolicy;
    private String basePath;
    private static final String DEFAULT_ROOT = "/registry";

    //不用zookeeper
    public void NettyClientProxy(ServiceAddress serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public NettyClientProxy(ServiceProvider serviceProvider, Class<?> serviceClass) {
        this.serviceProvider = serviceProvider;
        this.serviceClass = serviceClass;
    }

    public NettyClientProxy messageSerialize(MessageSerialize messageSerialize) {
        this.messageSerialize = messageSerialize;
        return this;
    }

    public NettyClientProxy balancePolicy(BalancePolicy balancePolicy) {
        this.balancePolicy = balancePolicy;
        return this;
    }

    public NettyClientProxy basePath(String basePath) {
        this.basePath = basePath;
        return this;
    }

    public NettyClientProxy build() {
        if (messageSerialize == null) {
            messageSerialize = new ProtoStuffMessageSerialize();
        }
        if(serviceAddress == null ) {
            if (StringUtil.isNullOrEmpty(basePath)) {
                basePath = DEFAULT_ROOT;
            }
            if(balancePolicy == null) {
                balancePolicy = new RandomBalancePolicy();
            }
            this.serviceName = AnnotationUtil.getRpcClientName(serviceClass);
        }
        return this;
    }

    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                new Class<?>[]{serviceClass}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MessageRequest messageRequest = transferMethod.buildRequest(method, args);
        NettyClient nettyClient = new NettyClient(messageRequest, messageSerialize);
        serviceAddress = serviceProvider.getServiceAddress(basePath, serviceName, balancePolicy);
        nettyClient.connect(serviceAddress);
        MessageResponse messageResponse = nettyClient.getClientChannelHandler().getMessageResponse();
        return messageResponse.getResult();
    }

    public ServiceAddress getServiceAddress() {
        return serviceAddress;
    }

    public String getServiceName() {
        return serviceName;
    }
}
