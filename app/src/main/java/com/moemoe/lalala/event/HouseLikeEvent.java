package com.moemoe.lalala.event;

/**
 * Created by Hygge on 2018/6/6.
 */

public class HouseLikeEvent {
    private String roleId;
    private int type;
    public HouseLikeEvent(String roleId,int type) {
        this.roleId = roleId;
        this.type=type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleId() {
        return roleId;
    }
}
