package com.moemoe.lalala.utils;

/**
 * Created by Hygge on 2018/4/24.
 */

public class ShareModel {
    private String text;
    private String url;
    private String imageUrl;

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
