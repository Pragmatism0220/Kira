package com.moemoe.lalala.event;

/**
 * Created by zhangyan on 2018/5/14.
 */


public class ScrollMessage {

    private int scrollY;

    public ScrollMessage(int scrollY) {
        this.scrollY = scrollY;
    }

    public int getScrollY() {
        return scrollY;
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }
}
