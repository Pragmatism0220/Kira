package com.moemoe.lalala.model.entity;

/**
 * Created by Administrator on 2018/6/12.
 */

public class saveRecordEntity {

    private String groupId;//主线剧情组ID，storyType=1时必填项 ,
    private String storyId;//主线或支线的剧情ID ,
    private int storyType;//剧情类型，1：主线剧情，2：支线剧情

    public saveRecordEntity(String groupId, String storyId, int storyType) {
        this.groupId = groupId;
        this.storyId = storyId;
        this.storyType = storyType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public int getStoryType() {
        return storyType;
    }

    public void setStoryType(int storyType) {
        this.storyType = storyType;
    }

    @Override
    public String toString() {
        return "saveRecordEntity{" +
                "groupId='" + groupId + '\'' +
                ", storyId='" + storyId + '\'' +
                ", storyType='" + storyType + '\'' +
                '}';
    }
}
