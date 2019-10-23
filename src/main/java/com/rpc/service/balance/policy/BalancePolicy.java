package com.rpc.service.balance.policy;

import com.rpc.net.ServiceAddress;

import java.util.List;

/**
 * Created by T440 on 2019/4/18.
 */
public interface BalancePolicy {

    /**
     * 负载均衡策略
     * @param serviceAddresses
     * @return
     */
    ServiceAddress select(List<ServiceAddress> serviceAddresses);

}
