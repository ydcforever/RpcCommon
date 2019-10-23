import com.rpc.zookeeper.provider.ZkServiceProvider;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by T440 on 2019/4/24.
 */
public class ProviderTest {

    public static void main(String[] args) {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("127.0.0.1:2181",
                new ExponentialBackoffRetry(1000, 3));
        curatorFramework.start();
        ZkServiceProvider zkServiceProvider = new ZkServiceProvider(curatorFramework);

    }
}
