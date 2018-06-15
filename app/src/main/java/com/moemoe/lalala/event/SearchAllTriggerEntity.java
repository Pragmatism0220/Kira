package com.moemoe.lalala.event;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.moemoe.lalala.model.entity.extraModel;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;

/**
 * Created by zhangyan on 2018/6/12.
 */
@Entity
public class SearchAllTriggerEntity {

    @Id
    private String id;//触发器ID
    @Transient
    private JsonArray condition;//条件数组,每种类型model不同
    private boolean force;//是否为强制剧情
    private String groupId;//主线剧情分组ID ,
    private String storyId;//关联剧情ID ,
    private int storyType;// 1:主线 2:支线
    private String type;//map:地图剧情,mobile:手机剧情
//    private String conditionStr;
//    @Convert(converter = extraConverter.class, columnType = String.class)
//    private extraModel extra;//地图扩展属性
//
//
//    @Generated(hash = 2023320779)
//    public SearchAllTriggerEntity(String id, boolean force, String groupId, String storyId,
//            int storyType, String type, String conditionStr, extraModel extra) {
//        this.id = id;
//        this.force = force;
//        this.groupId = groupId;
//        this.storyId = storyId;
//        this.storyType = storyType;
//        this.type = type;
//        this.conditionStr = conditionStr;
//        this.extra = extra;
//    }
//
//    @Generated(hash = 1974074649)
//    public SearchAllTriggerEntity() {
//    }
    @Generated(hash = 2101818343)
    public SearchAllTriggerEntity(String id, boolean force, String groupId, String storyId,
            int storyType, String type) {
        this.id = id;
        this.force = force;
        this.groupId = groupId;
        this.storyId = storyId;
        this.storyType = storyType;
        this.type = type;
    }
    @Generated(hash = 1974074649)
    public SearchAllTriggerEntity() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public boolean getForce() {
        return this.force;
    }
    public void setForce(boolean force) {
        this.force = force;
    }
    public String getGroupId() {
        return this.groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getStoryId() {
        return this.storyId;
    }
    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }
    public int getStoryType() {
        return this.storyType;
    }
    public void setStoryType(int storyType) {
        this.storyType = storyType;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }

//    public static class extraConverter implements PropertyConverter<extraModel, String> {
//
//        @Override
//        public extraModel convertToEntityProperty(String databaseValue) {
//            if (databaseValue == null) {
//                return null;
//            }
//            return new Gson().fromJson(databaseValue, extraModel.class);
//        }
//
//        @Override
//        public String convertToDatabaseValue(extraModel entityProperty) {
//            if (entityProperty == null) {
//                return null;
//            }
//            return new Gson().toJson(entityProperty);
//        }
//    }

//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public JsonArray getCondition() {
//        return condition;
//    }
//
//    public void setCondition(JsonArray condition) {
//        this.condition = condition;
//    }
//
//    public boolean isForce() {
//        return force;
//    }
//
//    public void setForce(boolean force) {
//        this.force = force;
//    }
//
//    public String getGroupId() {
//        return groupId;
//    }
//
//    public void setGroupId(String groupId) {
//        this.groupId = groupId;
//    }
//
//    public String getStoryId() {
//        return storyId;
//    }
//
//    public void setStoryId(String storyId) {
//        this.storyId = storyId;
//    }
//
//    public int getStoryType() {
//        return storyType;
//    }
//
//    public void setStoryType(int storyType) {
//        this.storyType = storyType;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public String getConditionStr() {
//        return conditionStr;
//    }
//
//    public void setConditionStr(String conditionStr) {
//        this.conditionStr = conditionStr;
//    }
//
//    public extraModel getExtra() {
//        return extra;
//    }
//
//    public void setExtra(extraModel extra) {
//        this.extra = extra;
//    }
//
//
//    @Override
//    public String toString() {
//        return "SearchAllTriggerEntity{" +
//                "id='" + id + '\'' +
//                ", condition=" + condition +
//                ", force=" + force +
//                ", groupId='" + groupId + '\'' +
//                ", storyId='" + storyId + '\'' +
//                ", storyType=" + storyType +
//                ", type='" + type + '\'' +
//                ", conditionStr='" + conditionStr + '\'' +
//                ", extra=" + extra +
//                '}';
//    }
//
//    public boolean getForce() {
//        return this.force;
//    }
}
