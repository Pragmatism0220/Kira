package com.moemoe.lalala.model.entity;

/**
 * Created by Administrator on 2018/5/29.
 */

public class PropInfoEntity {
    private String id;//道具id
    private String image;//道具图片
    private boolean isUserHadTool;//是否拥有道具  true 拥有道具
    private String name;//名称
    private int toolCount;//拥有道具个数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isUserHadTool() {
        return isUserHadTool;
    }

    public void setUserHadTool(boolean userHadTool) {
        isUserHadTool = userHadTool;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getToolCount() {
        return toolCount;
    }

    public void setToolCount(int toolCount) {
        this.toolCount = toolCount;
    }

    @Override
    public String toString() {
        return "PropInfoEntity{" +
                "id='" + id + '\'' +
                ", image='" + image + '\'' +
                ", isUserHadTool=" + isUserHadTool +
                ", name='" + name + '\'' +
                ", toolCount=" + toolCount +
                '}';
    }
}
