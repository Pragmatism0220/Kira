package com.moemoe.lalala.model.entity;

/**
 * Created by Hygge on 2018/6/23.
 */

public class HouseSleepEntity {
    private String id;
    private boolean isCompanion;
    private String name;
    private String roleOfId;
    private String whyCannotSleepWithYou;

    public void setId(String id) {
        this.id = id;
    }

    public void setCompanion(boolean companion) {
        isCompanion = companion;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoleOfId(String roleOfId) {
        this.roleOfId = roleOfId;
    }

    public void setWhyCannotSleepWithYou(String whyCannotSleepWithYou) {
        this.whyCannotSleepWithYou = whyCannotSleepWithYou;
    }

    public String getId() {
        return id;
    }

    public boolean isCompanion() {
        return isCompanion;
    }

    public String getName() {
        return name;
    }

    public String getRoleOfId() {
        return roleOfId;
    }

    public String getWhyCannotSleepWithYou() {
        return whyCannotSleepWithYou;
    }
}
