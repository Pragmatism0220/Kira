package com.moemoe.lalala.model.entity;

/**
 * Created by Administrator on 2018/7/20.
 */

public class ClothUpDateEvent {
    private int  position;

    public ClothUpDateEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
