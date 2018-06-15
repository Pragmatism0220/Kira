package com.moemoe.lalala.model.entity;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.renderscript.Script;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yi on 2016/11/29.
 */

public class HouseImage implements Parcelable {
    private final static String KEY_REAL_PATH = "real_path";
    private final static String KEY_LOCAL_PATH = "local_path";
    private final static String KEY_PATH = "path";
    private final static String KEY_W = "w";
    private final static String KEY_H = "h";
    private final static String KEY_ID = "id";
    private final static String KEY_ISSLEEP = "isSleep";
    private final static String KEY_ISROLETIMER = "isRoleTimer";

    private String real_path;
    private String local_path;
    @SerializedName("path")
    private String path;
    @SerializedName("w")
    private double w;
    @SerializedName("h")
    private double h;
    @SerializedName("size")
    private long size;
    @SerializedName("id")
    private String id;
    @SerializedName("isSleep")
    private boolean isSleep;
    @SerializedName("isRoleTimer")
    private boolean isRoleTimer;

    public void setRoleTimer(boolean roleTimer) {
        isRoleTimer = roleTimer;
    }

    public boolean isRoleTimer() {
        return isRoleTimer;
    }

    public void setSleep(boolean sleep) {
        isSleep = sleep;
    }

    public boolean isSleep() {
        return isSleep;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public String getReal_path() {
        return real_path;
    }

    public void setReal_path(String real_path) {
        this.real_path = real_path;
    }

    public String getLocal_path() {
        return local_path;
    }

    public void setLocal_path(String local_path) {
        this.local_path = local_path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public static final Creator<HouseImage> CREATOR = new Creator<HouseImage>() {
        @Override
        public HouseImage createFromParcel(Parcel in) {
            HouseImage image = new HouseImage();
            Bundle bundle;
            bundle = in.readBundle(getClass().getClassLoader());
            image.local_path = bundle.getString(KEY_LOCAL_PATH);
            image.path = bundle.getString(KEY_PATH);
            image.w = bundle.getInt(KEY_W);
            image.h = bundle.getInt(KEY_H);
            image.real_path = bundle.getString(KEY_REAL_PATH);
            image.size = bundle.getLong("size");
            image.id = bundle.getString(KEY_ID);
            image.isSleep = bundle.getBoolean(KEY_ISSLEEP);
            image.isRoleTimer = bundle.getBoolean(KEY_ISROLETIMER);
            return image;
        }

        @Override
        public HouseImage[] newArray(int size) {
            return new HouseImage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_LOCAL_PATH, local_path);
        bundle.putString(KEY_PATH, path);
        bundle.putDouble(KEY_W, w);
        bundle.putDouble(KEY_H, h);
        bundle.putString(KEY_REAL_PATH, real_path);
        bundle.putLong("size", size);
        bundle.putString(KEY_ID, id);
        bundle.putBoolean(KEY_ISSLEEP, isSleep);
        bundle.putBoolean(KEY_ISROLETIMER, isRoleTimer);
        dest.writeBundle(bundle);
    }
}
