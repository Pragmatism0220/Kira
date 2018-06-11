package com.moemoe.lalala.event;

/**
 * Created by Hygge on 2018/6/6.
 */

public class HouseLikeEvent {
    private String roleId;

    public HouseLikeEvent(String roleId) {
        this.roleId = roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleId() {
        return roleId;
    }
}
