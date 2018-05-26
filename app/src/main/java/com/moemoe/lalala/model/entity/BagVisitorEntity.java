package com.moemoe.lalala.model.entity;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Sora on 2018/3/6.
 */

public class BagVisitorEntity implements Parcelable {

    //    badge (徽章信息-返回, optional): 徽章 ,
//    id (string, optional): ID ,
//    level (integer, optional): 级别 ,
//    levelColor (string, optional): 级别颜色 ,
//    sex (string, optional): 性别 ,
//    signature (string, optional): 签名 ,
//    updateTime (string, optional): 书包访客时间 ,
//    userId (string, optional): 书包所属用户id ,
//    visitorUserIcon (string, optional): 书包访客用户头像 ,
//    visitorUserId (string, optional): 书包访客用户id
    private String id;
    private String updateTime;
    private String userId;
    private String visitorUserIcon;
    private String visitorUserId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;
    private ArrayList<BadgeEntity> badge;

    public void setBadge(ArrayList<BadgeEntity> badge) {
        this.badge = badge;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLevelColor(String levelColor) {
        this.levelColor = levelColor;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    private int level;

    public ArrayList<BadgeEntity> getBadge() {
        return badge;
    }

    public int getLevel() {
        return level;
    }

    public String getLevelColor() {
        return levelColor;
    }

    public String getSex() {
        return sex;
    }

    public String getSignature() {
        return signature;
    }

    private String levelColor;
    private String sex;
    private String signature;


    public void setId(String id) {
        this.id = id;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setVisitorUserIcon(String visitorUserIcon) {
        this.visitorUserIcon = visitorUserIcon;
    }

    public void setVisitorUserId(String visitorUserId) {
        this.visitorUserId = visitorUserId;
    }

    public String getId() {
        return id;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getUserId() {
        return userId;
    }

    public String getVisitorUserIcon() {
        return visitorUserIcon;
    }

    public String getVisitorUserId() {
        return visitorUserId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public static final Creator<BagVisitorEntity> CREATOR = new Creator<BagVisitorEntity>() {
        @Override
        public BagVisitorEntity createFromParcel(Parcel parcel) {
            BagVisitorEntity info = new BagVisitorEntity();
            Bundle bundle = parcel.readBundle(getClass().getClassLoader());
            info.id = bundle.getString("id");
            info.updateTime = bundle.getString("updateTime");
            info.userId = bundle.getString("userId");
            info.visitorUserIcon = bundle.getString("visitorUserIcon");
            info.visitorUserId = bundle.getString("visitorUserId");
            return info;
        }

        @Override
        public BagVisitorEntity[] newArray(int i) {
            return new BagVisitorEntity[0];
        }
    };
}
