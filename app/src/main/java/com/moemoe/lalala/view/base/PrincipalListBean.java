package com.moemoe.lalala.view.base;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */

public class PrincipalListBean {

    private String number;
    private String title;
    private boolean isLock;
    private List<Bitmap> extraImages;


    public PrincipalListBean(String number, String title, boolean isLock, List<Bitmap> extraImages) {
        this.number = number;
        this.title = title;
        this.isLock = isLock;
        this.extraImages = extraImages;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public List<Bitmap> getExtraImages() {
        return extraImages;
    }

    public void setExtraImages(List<Bitmap> extraImages) {
        this.extraImages = extraImages;
    }
}
