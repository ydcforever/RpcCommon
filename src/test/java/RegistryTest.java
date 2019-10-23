import com.rpc.zookeeper.registry.ZkServiceRegistry;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by T440 on 2019/4/18.
 */
public class RegistryTest {
    public static void main(String[] args) {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
        curatorFramework.start();
        ZkServiceRegistry zkServiceRegistry = new ZkServiceRegistry(curatorFramework);
//        zkServiceRegistry.register("com.ydc.ok", "127.0.0.1:222");
//        zkServiceRegistry.register("com.ydc.ok", "127.0.0.1:223");
        zkServiceRegistry.update("com.ydc.ok", "127.0.0.1:222", "/registry", "ok");
    }
}
