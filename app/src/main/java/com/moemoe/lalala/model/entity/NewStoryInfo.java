package com.moemoe.lalala.model.entity;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/6/11.
 */

public class NewStoryInfo {
    private String id;//主线剧情ID
    private ArrayList<String> images;//图片
    private boolean isLock;//是否解锁
    private String name;//名称
    private String scriptId;//剧情脚本ID
    private String sortId;//序号

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScriptId() {
        return scriptId;
    }

    public void setScriptId(String scriptId) {
        this.scriptId = scriptId;
    }

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }

    @Override
    public String toString() {
        return "NewStoryInfo{" +
                "id='" + id + '\'' +
                ", images=" + images +
                ", isLock=" + isLock +
                ", name='" + name + '\'' +
                ", scriptId='" + scriptId + '\'' +
                ", sortId='" + sortId + '\'' +
                '}';
    }
}
