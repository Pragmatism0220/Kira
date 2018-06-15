package com.moemoe.lalala.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhangyan on 2018/6/12.
 */
@Entity
public class extraModel {
    @Transient
    private int mapIconHeight;//高
    private String mapIconImage;//图标图片
    private int mapIconWidth;//宽
    private int mapLocX;//X坐标
    private int mapLocY;//Y坐标



    @Generated(hash = 1220603747)
    public extraModel(String mapIconImage, int mapIconWidth, int mapLocX,
            int mapLocY) {
        this.mapIconImage = mapIconImage;
        this.mapIconWidth = mapIconWidth;
        this.mapLocX = mapLocX;
        this.mapLocY = mapLocY;
    }

    @Generated(hash = 446298948)
    public extraModel() {
    }



    public int getMapIconHeight() {
        return mapIconHeight;
    }

    public void setMapIconHeight(int mapIconHeight) {
        this.mapIconHeight = mapIconHeight;
    }

    public String getMapIconImage() {
        return mapIconImage;
    }

    public void setMapIconImage(String mapIconImage) {
        this.mapIconImage = mapIconImage;
    }

    public int getMapIconWidth() {
        return mapIconWidth;
    }

    public void setMapIconWidth(int mapIconWidth) {
        this.mapIconWidth = mapIconWidth;
    }

    public int getMapLocX() {
        return mapLocX;
    }

    public void setMapLocX(int mapLocX) {
        this.mapLocX = mapLocX;
    }

    public int getMapLocY() {
        return mapLocY;
    }

    public void setMapLocY(int mapLocY) {
        this.mapLocY = mapLocY;
    }

    @Override
    public String toString() {
        return "extraModel{" +
                "mapIconHeight=" + mapIconHeight +
                ", mapIconImage='" + mapIconImage + '\'' +
                ", mapIconWidth=" + mapIconWidth +
                ", mapLocX=" + mapLocX +
                ", mapLocY=" + mapLocY +
                '}';
    }
}
