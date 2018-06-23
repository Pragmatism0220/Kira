package com.moemoe.lalala.event;

/**
 * Created by zhangyan on 2018/5/14.
 */


public class ScrollMessage {

    private int scrollY;
    private boolean isScroll;
    
    public ScrollMessage(int scrollY,boolean isScroll) {
        this.scrollY = scrollY;
        this.isScroll=isScroll;
    }

    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }

    public boolean isScroll() {
        return isScroll;
    }

    public int getScrollY() {
        return scrollY;
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }
}
