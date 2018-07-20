package com.moemoe.lalala.model.entity;

/**
 * Created by Hygge on 2018/6/1.
 */

public class ClothingEntity {
    
    private String condition;
    private String describe;
    private String id;
    private String image;
    private boolean isUse;
    private boolean isUserClothesHad;
    private String name;
    private String productId;

    private boolean clothNews;


    public boolean isClothNews() {
        return clothNews;
    }

    public void setClothNews(boolean clothNews) {
        this.clothNews = clothNews;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCondition(String conditio) {
        this.condition = conditio;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUse(boolean use) {
        isUse = use;
    }

    public void setUserClothesHad(boolean userClothesHad) {
        isUserClothesHad = userClothesHad;
    }

    public String getCondition() {
        return condition;
    }

    public String getDescribe() {
        return describe;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public boolean isUse() {
        return isUse;
    }

    public boolean isUserClothesHad() {
        return isUserClothesHad;
    }
}
