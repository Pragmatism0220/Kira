package com.moemoe.lalala.model.entity;

/**
 * Created by Administrator on 2018/6/12.
 */

public class NewJuQingTriggerImage {

    private String iconId;
    private String icon;
    private int x;
    private int y;
    private int h;
    private int w;
    private String md5;

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    @Override
    public String toString() {
        return "NewJuQingTriggerImage{" +
                "iconId='" + iconId + '\'' +
                ", icon='" + icon + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", h=" + h +
                ", w=" + w +
                ", md5='" + md5 + '\'' +
                '}';
    }
}
