package com.rpc.zookeeper.provider;

import com.rpc.net.ServiceAddress;
import com.rpc.service.balance.BalanceAdapter;
import com.rpc.service.balance.policy.BalancePolicy;
import com.rpc.service.management.ServiceProvider;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by T440 on 2019/4/15.
 */
public class ZkServiceProvider implements ServiceProvider {

    private static final Logger LOG = Logger.getLogger(ZkServiceProvider.class);

    private CuratorFramework curator;

    private final Map<String, List<ServiceAddress>> serviceAddressCache = new ConcurrentHashMap<>();

    private Set<ServiceAddress> ss;

    private Object lock = new Object();

    private boolean localFirst = true;

    public ZkServiceProvider(CuratorFramework curator) {
        this.curator = curator;
//        AddressListener addressListener = new AddressListener(curator,"/registry/AreaPartitionServiceImpl").build();
//        addressListener.start();
//        ss = addressListener.getServiceAddressCache();
//        addressListener.close();
    }

    public ZkServiceProvider localFirst(boolean localFirst) {
        this.localFirst = localFirst;
        return this;
    }

    /**
     * 节点监听
     * @param curator
     * @param path
     */
    public static void registerNodeCache(CuratorFramework curator, String path) {
        final NodeCache nodeCache = new NodeCache(curator, path, false);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                System.out.println("当前节点：" + nodeCache.getCurrentData());
            }
        });
        try {
            nodeCache.start(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, List<ServiceAddress>> findAllService(String basePath) {
        Map<String, List<ServiceAddress>> map = new ConcurrentHashMap<>();
        try {
            List<String> services = curator.getChildren().watched().forPath(basePath);
            for (String service : services) {
                List<ServiceAddress> addresses = findService(basePath, service);
                if (addresses != null) {
                    map.putIfAbsent(service, addresses);
                }
            }
        } catch (Exception e) {
            LOG.info("无法获取服务列表");
        }
        return map;
    }

    @Override
    public List<ServiceAddress> findService(String basePath, String serviceName) {
        List<ServiceAddress> serviceAddresses = new ArrayList<>();
        try {
            List<String> data = curator.getChildren().watched().forPath(basePath + "/" + serviceName);
            for (String address : data)
                serviceAddresses.add(new ServiceAddress(address));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceAddresses;
    }

    @Override
    public ServiceAddress getServiceAddress(String basePath, String serviceName, BalancePolicy balancePolicy) {
        List<ServiceAddress> serviceAddresses = findService(basePath, serviceName);
        BalanceAdapter balanceAdapter = new BalanceAdapter(serviceAddresses, localFirst);
        return balanceAdapter.getServiceAddress(balancePolicy);
    }
}
