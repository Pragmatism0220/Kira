package com.moemoe.lalala.event;

/**
 *
 * Created by Sora on 2018/2/7.
 */

public class FeedFollowOtherEvent {
    private int num;
    private String id;

    public FeedFollowOtherEvent(int num, String id) {
        this.num = num;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
