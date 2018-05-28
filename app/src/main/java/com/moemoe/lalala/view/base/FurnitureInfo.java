package com.moemoe.lalala.view.base;

/**
 * Created by Administrator on 2018/5/22.
 */

public class FurnitureInfo {
    private String name;
    private String style;
    private boolean isUse;


    public FurnitureInfo() {
    }

    public FurnitureInfo(String name, String style, boolean isUse) {
        this.name = name;
        this.style = style;
        this.isUse = isUse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public boolean isUse() {
        return isUse;
    }

    public void setUse(boolean use) {
        isUse = use;
    }

    @Override
    public String toString() {
        return "FurnitureInfo{" +
                "name='" + name + '\'' +
                ", style='" + style + '\'' +
                ", isUse=" + isUse +
                '}';
    }
}
