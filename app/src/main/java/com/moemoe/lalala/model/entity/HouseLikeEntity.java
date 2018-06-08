package com.moemoe.lalala.model.entity;

/**
 * Created by Hygge on 2018/6/5.
 */

public class HouseLikeEntity {
    private String id;
    private boolean isCollectFull;
    private boolean isCollectable;
    private boolean isSleep;
    private String lastCollectTime;
    private long remainTime;
    private String roleId;
    private int roleLike;

    public String getId() {
        return id;
    }

    public boolean isCollectFull() {
        return isCollectFull;
    }

    public boolean isCollectable() {
        return isCollectable;
    }

    public boolean isSleep() {
        return isSleep;
    }

    public String getLastCollectTime() {
        return lastCollectTime;
    }

    public long getRemainTime() {
        return remainTime;
    }

    public String getRoleId() {
        return roleId;
    }

    public int getRoleLike() {
        return roleLike;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCollectFull(boolean collectFull) {
        isCollectFull = collectFull;
    }

    public void setCollectable(boolean collectable) {
        isCollectable = collectable;
    }

    public void setSleep(boolean sleep) {
        isSleep = sleep;
    }

    public void setLastCollectTime(String lastCollectTime) {
        this.lastCollectTime = lastCollectTime;
    }

    public void setRemainTime(long remainTime) {
        this.remainTime = remainTime;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setRoleLike(int roleLike) {
        this.roleLike = roleLike;
    }
}
