package com.rpc.service.management;

/**
 * Created by T440 on 2019/1/31.
 */
public interface ServiceRegistry {

    /**
     * 服务注册
     * @param serviceName
     * @param serviceAddress
     * @param basePath
     * @param data
     */
    void register(String serviceName, String serviceAddress, String basePath, String data);

    /**
     * 服务更新
     * @param serviceName
     * @param serviceAddress
     * @param basePath
     * @param data
     */
    void update(String serviceName, String serviceAddress, String basePath, String data);

    /**
     * 服务下线
     * @param serviceName
     * @param serviceAddress
     * @param basePath
     */
    void delete(String serviceName, String serviceAddress, String basePath);

}
