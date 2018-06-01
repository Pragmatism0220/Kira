package com.moemoe.lalala.model.entity;

/**
 * Created by Administrator on 2018/5/31.
 */

public class VisitorsEntityRequest {
    private int size;//一页返回多少条
    private int start;//从多少条开始

    public VisitorsEntityRequest(int size, int start) {
        this.size = size;
        this.start = start;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return "VisitorsEntityRequest{" +
                "size=" + size +
                ", start=" + start +
                '}';
    }
}
