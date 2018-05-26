package com.moemoe.lalala.service;

import android.widget.ImageView;

/**
 * Created by Administrator on 2018/5/8.
 */

public class TestRole {
    private String mName;
    private ImageView mImage;

    public TestRole() {

    }

    public TestRole(String mName, ImageView mImage) {
        this.mName = mName;
        this.mImage = mImage;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public ImageView getmImage() {
        return mImage;
    }

    public void setmImage(ImageView mImage) {
        this.mImage = mImage;
    }

    @Override
    public String toString() {
        return "TestRole{" +
                "mName='" + mName + '\'' +
                ", mImage=" + mImage +
                '}';
    }
}
