package com.moemoe.lalala.model.entity;

/**
 * Created by Administrator on 2018/5/31.
 */

public class SaveVisitorEntity {
    private String roleId;//角色ID
    private String hostId;//房主ID
    private int visitorType;//1、拜访宅屋  2、戳破莲的气球 3、收集了你房间的垃圾

    public SaveVisitorEntity(String roleId, String hostId, int visitorType) {
        this.roleId = roleId;
        this.hostId = hostId;
        this.visitorType = visitorType;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public int getVisitorType() {
        return visitorType;
    }

    public void setVisitorType(int visitorType) {
        this.visitorType = visitorType;
    }

    @Override
    public String toString() {
        return "SaveVisitorEntity{" +
                "roleId='" + roleId + '\'' +
                ", hostId='" + hostId + '\'' +
                ", visitorType=" + visitorType +
                '}';
    }
}
