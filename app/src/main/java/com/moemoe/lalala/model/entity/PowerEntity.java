package com.moemoe.lalala.model.entity;

/**
 * Created by Administrator on 2018/7/4.
 */

public class PowerEntity {

    private int healthPoint;//用户体力值 ,
    private int maxHealthPoint;//体力最大值
    private int visitorCount;//宅屋房间的访客点赞数

    public int getHealthPoint() {
        return healthPoint;
    }

    public void setHealthPoint(int healthPoint) {
        this.healthPoint = healthPoint;
    }

    public int getMaxHealthPoint() {
        return maxHealthPoint;
    }

    public void setMaxHealthPoint(int maxHealthPoint) {
        this.maxHealthPoint = maxHealthPoint;
    }

    public int getVisitorCount() {
        return visitorCount;
    }

    public void setVisitorCount(int visitorCount) {
        this.visitorCount = visitorCount;
    }
}
