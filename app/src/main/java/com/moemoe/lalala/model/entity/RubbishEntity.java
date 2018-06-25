package com.moemoe.lalala.model.entity;

/**
 * Created by Hygge on 2018/6/24.
 */

public class RubbishEntity {
    private String describe;
    private String id;
    private String image;
    private String name;
    private int type;
    private String scriptId;

    public void setScriptId(String scriptId) {
        this.scriptId = scriptId;
    }

    public String getScriptId() {
        return scriptId;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }
}
