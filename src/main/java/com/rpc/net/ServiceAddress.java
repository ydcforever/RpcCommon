package com.rpc.net;

import java.net.InetSocketAddress;

/**
 * Created by T440 on 2019/1/31.
 */
public class ServiceAddress {
    private String hostname;
    private int port;
    private int weight;
    private String address;

    public ServiceAddress(String address) {
        this.address = address;
        String[] addr = address.split(":");
        this.hostname = addr[0];
        this.port = Integer.parseInt(addr[1]);
        this.weight = addr.length == 3 ? Integer.parseInt(addr[2]) : 1;
    }

    public InetSocketAddress getInetSocketAddress() {
        return new InetSocketAddress(this.hostname, this.port);
    }

    public String getHostname() {
        return this.hostname;
    }

    public int getPort() {
        return this.port;
    }

    public int getWeight() {
        return this.weight;
    }

    public String getAddress() {
        return address;
    }
}
