package com.moemoe.lalala.model.entity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.ArrayList;

/**
 * Created by Administrator on 2018/6/12.
 */

public class NewJuQingTriggerEntity {

    private String id;
    private JsonArray condition;
    private String extra;
    private boolean force;//是否为强制剧情
    private int level;//1:主线 2:支线 3:日常 ,
    private String roleOf;//关联的角色
    private String storyId;//关联剧情ID
    private String type;//map:地图剧情,mobile:手机剧情
    private String conditionStr;
    private String groupId;//主线剧情分组ID

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JsonArray getCondition() {
        return condition;
    }

    public void setCondition(JsonArray condition) {
        this.condition = condition;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getRoleOf() {
        return roleOf;
    }

    public void setRoleOf(String roleOf) {
        this.roleOf = roleOf;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConditionStr() {
        return conditionStr;
    }

    public void setConditionStr(String conditionStr) {
        this.conditionStr = conditionStr;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "NewJuQingTriggerEntity{" +
                "id='" + id + '\'' +
                ", condition=" + condition +
                ", extra='" + extra + '\'' +
                ", force=" + force +
                ", level=" + level +
                ", roleOf='" + roleOf + '\'' +
                ", storyId='" + storyId + '\'' +
                ", type='" + type + '\'' +
                ", conditionStr='" + conditionStr + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }
}
