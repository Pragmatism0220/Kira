package com.moemoe.lalala.model.entity;

/**
 * Created by yi on 2017/9/5.
 */

public class PhoneMenuEntity {

    private String userIcon;
    private String userName;
    private String userId;
    private String userSex;
    private String mark;
    private String signature;
    private boolean follow;
    private String bg;
    private String levelColor;
    private int level;
    private BadgeEntity badge;
    private boolean vip;
    private boolean eachFollow;
    private String rcTargetId;

    public boolean isEachFollow() {
        return eachFollow;
    }

    public void setEachFollow(boolean eachFollow) {
        this.eachFollow = eachFollow;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getMark() {
        return mark;
    }

    public String getSignature() {
        return signature;
    }

    public boolean isFollow() {
        return follow;
    }

    public String getBg() {
        return bg;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    public void setLevelColor(String levelColor) {
        this.levelColor = levelColor;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setBadge(BadgeEntity badge) {
        this.badge = badge;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public String getLevelColor() {
        return levelColor;
    }

    public int getLevel() {
        return level;
    }

    public BadgeEntity getBadge() {
        return badge;
    }

    public boolean isVip() {
        return vip;
    }

    public String getRcTargetId() {
        return rcTargetId;
    }

    public void setRcTargetId(String rcTargetId) {
        this.rcTargetId = rcTargetId;
    }
}
