package com.rpc.service.balance.policy.impl;

import com.rpc.net.ServiceAddress;
import com.rpc.service.balance.policy.BalancePolicy;

import java.util.List;

/**
 * Created by T440 on 2019/4/18.
 */
public class RandomBalancePolicy implements BalancePolicy {
    @Override
    public ServiceAddress select(List<ServiceAddress> serviceAddresses) {
        int len = serviceAddresses.size();
        int balanceId = (int)(Math.random() * len);
        return serviceAddresses.get(balanceId);
    }
}
