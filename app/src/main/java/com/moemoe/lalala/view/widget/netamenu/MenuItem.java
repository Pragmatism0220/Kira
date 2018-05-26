package com.moemoe.lalala.view.widget.netamenu;

import android.support.annotation.DrawableRes;

/**
 * Created by yi on 2017/6/6.
 */

public class MenuItem {

    private int id;
    private String text;
    private int ImgId;
    private boolean isTextColor;

    public MenuItem() {

    }

    public MenuItem(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public MenuItem(int id, String text, @DrawableRes int imgId) {
        this.id = id;
        this.text = text;
        ImgId = imgId;
    }

    public MenuItem(int id, String text, @DrawableRes int imgId, boolean isTextColor) {
        this.id = id;
        this.text = text;
        ImgId = imgId;
        this.isTextColor = isTextColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImgId() {
        return ImgId;
    }

    public void setImgId(int imgId) {
        ImgId = imgId;
    }

    public boolean isTextColor() {
        return isTextColor;
    }

    public void setTextColor(boolean textColor) {
        isTextColor = textColor;
    }
}
