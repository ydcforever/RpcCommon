package com.rpc.zookeeper.provider;

import com.rpc.net.ServiceAddress;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by T440 on 2019/4/24.
 */
public class AddressListener implements Closeable{
    private static final Logger LOG = Logger.getLogger(AddressListener.class);

    private volatile Set<ServiceAddress> serviceAddressCache = new HashSet<>();

    private CuratorFramework curator;

    private String parentPath;

    /**
     * 监听次数
     */
    private CountDownLatch countDownLatch= new CountDownLatch(3);

    private final Object lock = new Object();

    private boolean cacheData = true;

    private PathChildrenCache pathChildrenCache;

    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public AddressListener(CuratorFramework curator, String parentPath) {
        this.curator = curator;
        this.parentPath = parentPath;
    }

    public AddressListener cacheData(boolean cacheData) {
        this.cacheData = cacheData;
        return this;
    }

    public AddressListener build() {
        if (curator.getState() == CuratorFrameworkState.LATENT) {
            curator.start();
        }
        this.pathChildrenCache = new PathChildrenCache(curator, parentPath, cacheData, false, executorService);
        return this;
    }

    public void start() {
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                PathChildrenCacheEvent.Type eventType = pathChildrenCacheEvent.getType();
                switch (eventType) {
                    case CONNECTION_RECONNECTED:
                        LOG.info("Connection is reconection.");
                        break;
                    case CONNECTION_SUSPENDED:
                        LOG.info("Connection is suspended.");
                        break;
                    case CONNECTION_LOST:
                        LOG.warn("Connection error,waiting...");
                        return;
                    case INITIALIZED:
                        LOG.warn("Connection init ...");
                    default:
                }
                updateServiceCache();
                countDownLatch.countDown();
            }
        }, executorService);
        try {
            pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void updateServiceCache() {
        List<ChildData> list = pathChildrenCache.getCurrentData();
        synchronized (lock) {
            serviceAddressCache.clear();
            for(ChildData childData : list) {
                String address = childData.getPath().replace(parentPath + "/","");
                LOG.info("服务有变" + address);
                ServiceAddress serviceAddress = new ServiceAddress(address);
                serviceAddressCache.add(serviceAddress);
            }
        }
    }

    @Override
    public void close() {
        try {
            pathChildrenCache.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<ServiceAddress> getServiceAddressCache() {
        return serviceAddressCache;
    }
}
