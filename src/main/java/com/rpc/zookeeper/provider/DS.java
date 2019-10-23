package com.rpc.zookeeper.provider;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;

/**
* Created by T440 on 2019/4/15.
*/
public class DS {
    public static void main(String[] args) {
        CuratorFramework client = CuratorFrameworkFactory.newClient("l27.0.0.1:2181",
                new ExponentialBackoffRetry(1, 1));
        client.start();
        try {
            client.blockUntilConnected();

            ServiceDiscovery<String> serviceDiscovery = ServiceDiscoveryBuilder.builder(String.class)
                    .client(client)
                    .basePath("/registry")
                   .serializer(new JsonInstanceSerializer<>(String.class))
                    .build();

            serviceDiscovery.start();
            System.out.println(1);
//            Collection<String> serviceNames = serviceDiscovery.queryForNames();
//            for (String serviceName : serviceNames) {
//                System.out.println(serviceName);
//            }
            //根据名称获取服务
            Collection<ServiceInstance<String>> services = serviceDiscovery.queryForInstances("/ydc");
            System.out.println(services.size());
            for(ServiceInstance<String> service : services) {
                System.out.println(service.getPayload());
                System.out.println(service.getAddress() + "\t" + service.getPort());
                System.out.println("---------------------");
            }

            serviceDiscovery.close();
            client.close();

        } catch (Exception e) {

        }
    }
}
