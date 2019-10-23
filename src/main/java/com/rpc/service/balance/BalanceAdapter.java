package com.rpc.service.balance;

import com.rpc.net.LocalNetwork;
import com.rpc.net.ServiceAddress;
import com.rpc.service.balance.policy.BalancePolicy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by T440 on 2019/1/31.
 */
public class BalanceAdapter {

    private List<ServiceAddress> addressList;

    public BalanceAdapter(List<ServiceAddress> serviceAddresses, boolean localFirst) {
        this.addressList = new ArrayList<ServiceAddress>();
        if (localFirst) {
            String localIp = LocalNetwork.findLocalTp();
            for(ServiceAddress serviceAddress : serviceAddresses) {
                if(serviceAddress.getHostname().equals(localIp)) {
                    this.addressList.add(serviceAddress);
                }
            }
        }
        if(this.addressList.size() == 0) {
            this.addressList = serviceAddresses;
        }
    }

    public ServiceAddress getServiceAddress(BalancePolicy balancePolicy) {
         return balancePolicy.select(addressList);
    }
}
