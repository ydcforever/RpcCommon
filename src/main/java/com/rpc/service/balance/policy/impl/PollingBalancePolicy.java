package com.rpc.service.balance.policy.impl;

import com.rpc.net.ServiceAddress;
import com.rpc.service.balance.policy.BalancePolicy;

import java.util.List;

public class PollingBalancePolicy  implements BalancePolicy {
	
	private int i = 0;
	
    @Override
    public ServiceAddress select(List<ServiceAddress> serviceAddresses) {
        int len = serviceAddresses.size();
        int balanceId = i % len;
        i++;
        return serviceAddresses.get(balanceId);
    }
}
