package com.moemoe.lalala.model.entity;

/**
 * Created by Administrator on 2018/5/31.
 */

public class VisitorsEntity {
    private String createTime;//来访时间
    private String id;//访客信息ID ,
    private String roleId;//角色，访客如果操作了宅屋角色，则该值不为空，即visitorType=2
    private String roleName;// 角色名称，访客如果操作了宅屋角色，则该值不为空，即visitorType=2。 ,
    private String visitorId;//访客用户ID
    private String visitorImage;//访客头像
    private int visitorLevel;//访客等级
    private String visitorLevelColor;//访客等级对应的颜色
    private String visitorSex;//访客性别
    private int visitorType;//访客操作类型
    private String titleTime;
    private String rightTime;
    private int  count;//访客总数

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    public String getVisitorImage() {
        return visitorImage;
    }

    public void setVisitorImage(String visitorImage) {
        this.visitorImage = visitorImage;
    }

    public int getVisitorLevel() {
        return visitorLevel;
    }

    public void setVisitorLevel(int visitorLevel) {
        this.visitorLevel = visitorLevel;
    }

    public String getVisitorLevelColor() {
        return visitorLevelColor;
    }

    public void setVisitorLevelColor(String visitorLevelColor) {
        this.visitorLevelColor = visitorLevelColor;
    }

    public String getVisitorSex() {
        return visitorSex;
    }

    public void setVisitorSex(String visitorSex) {
        this.visitorSex = visitorSex;
    }

    public int getVisitorType() {
        return visitorType;
    }

    public void setVisitorType(int visitorType) {
        this.visitorType = visitorType;
    }

    public String getTitleTime() {
        return titleTime;
    }

    public void setTitleTime(String titleTime) {
        this.titleTime = titleTime;
    }

    public String getRightTime() {
        return rightTime;
    }

    public void setRightTime(String rightTime) {
        this.rightTime = rightTime;
    }

    @Override
    public String toString() {
        return "VisitorsEntity{" +
                "createTime='" + createTime + '\'' +
                ", id='" + id + '\'' +
                ", roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
                ", visitorId='" + visitorId + '\'' +
                ", visitorImage='" + visitorImage + '\'' +
                ", visitorLevel=" + visitorLevel +
                ", visitorLevelColor='" + visitorLevelColor + '\'' +
                ", visitorSex='" + visitorSex + '\'' +
                ", visitorType=" + visitorType +
                ", titleTime='" + titleTime + '\'' +
                ", rightTime='" + rightTime + '\'' +
                '}';
    }
}
