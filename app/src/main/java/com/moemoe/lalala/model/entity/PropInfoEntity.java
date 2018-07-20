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
    private String describe;//道具信息描述
    private String productId;//商品ID
    private int count;

    private boolean showNews;

    public boolean isShowNews() {
        return showNews;
    }

    public void setShowNews(boolean showNews) {
        this.showNews = showNews;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected = false; //item选中 自加入

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

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

}
