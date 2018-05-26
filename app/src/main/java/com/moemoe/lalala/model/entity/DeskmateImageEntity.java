package com.moemoe.lalala.model.entity;

/**
 * Created by Hygge on 2018/5/23.
 */

public class DeskmateImageEntity {

    private int h;
    private String path;
    private String remark;
    private int size;
    private int w;
    private String md5;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public String getPath() {
        return path;
    }

    public String getRemark() {
        return remark;
    }

    public int getSize() {
        return size;
    }

    public int getW() {
        return w;
    }
}
