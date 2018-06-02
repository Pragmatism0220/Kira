package com.moemoe.lalala.event;

/**
 * Created by Hygge on 2018/6/2.
 */

public class KiraTabLayoutEvent {
    private int position;

    public KiraTabLayoutEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
