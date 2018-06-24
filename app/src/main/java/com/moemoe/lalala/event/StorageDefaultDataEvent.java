package com.moemoe.lalala.event;

/**
 * Created by zhangyan on 2018/6/19.
 */

public class StorageDefaultDataEvent {

    private boolean isFurniture;

    public StorageDefaultDataEvent(boolean isFurniture) {
        this.isFurniture = isFurniture;
    }

    public boolean isFurniture() {
        return isFurniture;
    }
}
