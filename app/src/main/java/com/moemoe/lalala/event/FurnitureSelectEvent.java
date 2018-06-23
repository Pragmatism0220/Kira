package com.moemoe.lalala.event;

/**
 * Created by zhangyan on 2018/6/23.
 */

public class FurnitureSelectEvent {
    private String id;

    public FurnitureSelectEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id == null ? "" :id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
