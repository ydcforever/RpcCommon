package com.rpc.zookeeper.registry;

import com.rpc.net.ServiceAddress;
import com.rpc.service.management.ServiceRegistry;
import org.apache.curator.framework.CuratorFramework;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.Map;

/**
 * Created by T440 on 2019/1/31.
 */
public class ZkServiceRegistry implements ServiceRegistry {

    private static final Logger LOG = Logger.getLogger(ZkServiceRegistry.class);

    private static final String DEFAULT_ROOT = "/registry";

    private CuratorFramework curator;

    public ZkServiceRegistry(CuratorFramework curator) {
        this.curator = curator;
    }

    public void register(Map<String, ServiceAddress> services) {
        for (Map.Entry<String, ServiceAddress> entry : services.entrySet()) {
            register(entry.getKey(), entry.getValue().getAddress());
        }
    }

    public void register(String serviceName, String serviceAddress) {
        register(serviceName, serviceAddress, DEFAULT_ROOT, "");
    }

    @Override
    public void register(String serviceName, String serviceAddress, String basePath, String data) {
        try {
            String addressPath = basePath  + "/" + serviceName + "/" + serviceAddress;
            Stat stat = curator.checkExists().forPath(addressPath);
            if(stat == null) {
                curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(addressPath, data.getBytes());
                LOG.info("create address node:{} => {}" + addressPath);
            } else {
                LOG.info("recreate address node!");
            }
        } catch (Exception e) {
            LOG.error("create node failure", e);
        }
    }

    @Override
    public void update(String serviceName, String serviceAddress, String basePath, String data) {
        try {
            String addressPath = basePath  + "/" + serviceName + "/" + serviceAddress;
            curator.setData().forPath(addressPath, data.getBytes());
            LOG.info("update address node:{} => {}" + addressPath);
        } catch (Exception e) {
            LOG.error("update node failure", e);
        }
    }

    @Override
    public void delete(String serviceName, String serviceAddress, String basePath) {
        try {
            String addressPath = basePath  + "/" + serviceName + "/" + serviceAddress;
            curator.delete().forPath(addressPath);
            LOG.info("delete address node:{} => {}" + addressPath);
        } catch (Exception e) {
            LOG.error("delete node failure", e);
        }
    }
}
