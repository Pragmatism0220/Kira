package com.moemoe.lalala.model.entity;

import com.google.gson.JsonObject;

import org.json.JSONObject;

/**
 * Created by Sora on 2018/3/8.
 */

public class SearchNormalEntity {

    private String type;
    private String data;

    public void setType(String type) {
        this.type = type;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public String getData() {
        return data;
    }
}
