package com.rpc.zookeeper.update;

/**
 * Created by ydc on 2019/5/7.
 */
public interface KeepConnectionUpdate {

    public void setCnt(int cnt);

    public void addBalance(Integer step);

    public void reduceBalance(Integer step);

}
