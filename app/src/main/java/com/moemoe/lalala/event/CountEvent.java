package com.moemoe.lalala.event;

/**
 * Created by Administrator on 2018/7/13.
 */

public class CountEvent {
    private int  count;
    private String docId;

    public CountEvent(int count, String docId) {
        this.count = count;
        this.docId = docId;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
