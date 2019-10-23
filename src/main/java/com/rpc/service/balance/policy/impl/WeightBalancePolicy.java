package com.rpc.service.balance.policy.impl;

import com.rpc.net.ServiceAddress;
import com.rpc.service.balance.policy.BalancePolicy;

import java.util.List;

/**
 * Created by T440 on 2019/4/18.
 */
public class WeightBalancePolicy implements BalancePolicy {

    /**上次选择的服务器*/
    private int currentIndex = -1;
    /**当前调度的权值*/
    private int currentWeight = 0;
    /**最大权重*/
    private int maxWeight;
    /**权重的最大公约数*/
    private int gcdWeight;
    /**服务器数*/
    private int serverCount;

    public ServiceAddress select1(List<ServiceAddress> serviceAddresses) {
        int len = serviceAddresses.size();
        int min_weight = serviceAddresses.get(0).getWeight();
        int balanceId = 0;
        for(int i = 0; i < len; ++i) {
            int weight = serviceAddresses.get(i).getWeight();
            if(weight < min_weight) {
                min_weight = weight;
                balanceId = i;
            }
        }
        return serviceAddresses.get(balanceId);
    }
    @Override
    public ServiceAddress select(List<ServiceAddress> servers){

        maxWeight = greatestWeight(servers);
        gcdWeight = greatestCommonDivisor(servers);
        serverCount = servers.size();

        while(true){
            currentIndex = (currentIndex + 1) % serverCount;
            if(currentIndex == 0){
                currentWeight = currentWeight - gcdWeight;
                if(currentWeight <= 0){
                    currentWeight = maxWeight;
                    if(currentWeight == 0){
                        return null;
                    }
                }
            }
            if(servers.get(currentIndex).getWeight() >= currentWeight){
                return servers.get(currentIndex);
            }
        }
    } 
    
    /*
     * 得到两值的最大公约数
     */

    public int greaterCommonDivisor(int a, int b){
        if(a % b == 0){
            return b;
        }else{
            return greaterCommonDivisor(b,a % b);
        }
    }

    /*
     * 得到list中所有权重的最大公约数，实际上是两两取最大公约数d，然后得到的d
     * 与下一个权重取最大公约数，直至遍历完
     */
    public int greatestCommonDivisor(List<ServiceAddress> servers){
        int divisor = 0;
        for(int index = 0, len = servers.size(); index < len - 1; index++){
            if(index ==0){
                divisor = greaterCommonDivisor(
                        servers.get(index).getWeight(), servers.get(index + 1).getWeight());
            }else{
                divisor = greaterCommonDivisor(divisor, servers.get(index).getWeight());
            }
        }
        return divisor;
    }

    /*
     * 得到list中的最大的权重
     */
    public int greatestWeight(List<ServiceAddress> servers){
        int weight = 0;
        for(ServiceAddress server : servers){
            if(weight < server.getWeight()){
                weight = server.getWeight();
            }
        }
        return weight;
    }

}
