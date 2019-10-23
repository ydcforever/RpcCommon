package com.rpc.service.management;

import com.rpc.net.ServiceAddress;
import com.rpc.service.balance.policy.BalancePolicy;

import java.util.List;
import java.util.Map;

/**
 * Created by T440 on 2019/1/31.
 */
public interface ServiceProvider {

    /**
     * 所有服务地址列表
     * @param basePath
     * @return
     * @throws Exception
     */
    Map<String, List<ServiceAddress>> findAllService(String basePath) throws Exception;

    /**
     * 服务地址列表
     * @param basePath
     * @param serviceName
     * @return
     * @throws Exception
     */
    List<ServiceAddress> findService(String basePath, String serviceName) throws Exception;

    /**
     * 负载均衡
     * @param basePath
     * @param serviceName
     * @param balancePolicy
     * @return
     */
    ServiceAddress getServiceAddress(String basePath, String serviceName, BalancePolicy balancePolicy);

}
