package com.rpc.zookeeper.registry;

import com.rpc.net.ServiceAddress;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.*;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
* Created by T440 on 2019/4/15.
*/
public class SR {
    private ServiceDiscovery<ServiceAddress> serviceDiscovery;

    public static void main(String[] args) {
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181",
                new ExponentialBackoffRetry(1000, 3));
        client.start();
        try {
//            client.blockUntilConnected();
            ServiceInstanceBuilder<String> host1 = ServiceInstance.builder();
            host1.name("ydc")
                    .id("kk")
                    .port(1001)
                    .address("127.0.0.1")
                    .payload("lsoa")
                    .serviceType(ServiceType.PERMANENT);
//                    .uriSpec(new UriSpec("{scheme}://{address}:{port}"));

            ServiceInstance<String> instance = host1.build();

            ServiceDiscovery<String> serviceDiscovery = ServiceDiscoveryBuilder.builder(String.class)
                    .client(client)
                   .serializer(new JsonInstanceSerializer<String>(String.class))
                    .basePath("/registry")
                    .build();

            //服务注册
            serviceDiscovery.registerService(instance);
            serviceDiscovery.start();

            TimeUnit.SECONDS.sleep(100);

            serviceDiscovery.close();
            client.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerService(ServiceInstance<ServiceAddress> instance) throws Exception {
        serviceDiscovery.registerService(instance);
    }

    public void updateService(ServiceInstance<ServiceAddress> instance) throws Exception {
        serviceDiscovery.updateService(instance);
    }

    public void unregisterService(ServiceInstance<ServiceAddress> instance) throws Exception {
        serviceDiscovery.unregisterService(instance);
    }

    public Collection<ServiceInstance<ServiceAddress>> queryForInstances(String name) throws Exception {
        return serviceDiscovery.queryForInstances(name);
    }

    public ServiceInstance<ServiceAddress> queryForInstance(String name, String id) throws Exception {
        return serviceDiscovery.queryForInstance(name, id);
    }

    public void start() throws Exception {
        serviceDiscovery.start();
    }

    public void close() throws Exception {
        serviceDiscovery.close();
    }
}
