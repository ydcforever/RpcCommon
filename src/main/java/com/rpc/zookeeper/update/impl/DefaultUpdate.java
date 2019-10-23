package com.rpc.zookeeper.update.impl;

import com.rpc.zookeeper.update.KeepConnectionUpdate;
/**
 * Created by ydc on 2019/5/7.
 */
public class DefaultUpdate implements KeepConnectionUpdate {

    private int cnt;

    @Override
    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    @Override
    public void addBalance(Integer step) {
        cnt++;
    }

    @Override
    public void reduceBalance(Integer step) {
        cnt--;
    }
}
