package com.moemoe.lalala.model.entity;

/**
 * Created by Administrator on 2018/5/28.
 */

public class RoleInfoEntity {
    private String headIcon;//角色头像图片，不带七牛前缀
    private String headIconMd5;// 角色头像图片md5
    private int headIconOrigHeight;// 角色头像原始高 ,
    private int headIconOrigWidth;//角色头像原始宽
    private String id;// 角色ID
    private boolean isUserDeskmate;//同桌，true：是同桌（用户只有一个同桌
    private boolean isUserHadRole;//改角色头像用户是否拥有，true：拥有 ,
    private int maxPutInHouseNum;//用户最多可以放入宅屋里面的角色个数   4
    private String name;//角色名称
    private String roleType;// 角色类型
    private int userLikeRoleDefine; //用户将角色房屋宅屋里收集的好感度累计值 ,
    private String userLikeRoleDefineTxt;//用户将角色房屋宅屋里收集的好感度累计值对应的好感评价
    private String showHeadIcon;//大图
    private boolean isPutInHouse;// 是否放入宅屋，true：已经放入宅屋
    private boolean isSelected = false; //item选中 自加入
    private int count;
    private String roleNumber;

    public String getRoleNumber() {
        return roleNumber;
    }

    public void setRoleNumber(String roleNumber) {
        this.roleNumber = roleNumber;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public boolean isPutInHouse() {
        return isPutInHouse;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean getIsPutInHouse() {
        return isPutInHouse;
    }

    public void setPutInHouse(boolean putInHouse) {
        isPutInHouse = putInHouse;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public String getHeadIconMd5() {
        return headIconMd5;
    }

    public void setHeadIconMd5(String headIconMd5) {
        this.headIconMd5 = headIconMd5;
    }

    public int getHeadIconOrigHeight() {
        return headIconOrigHeight;
    }

    public void setHeadIconOrigHeight(int headIconOrigHeight) {
        this.headIconOrigHeight = headIconOrigHeight;
    }

    public int getHeadIconOrigWidth() {
        return headIconOrigWidth;
    }

    public void setHeadIconOrigWidth(int headIconOrigWidth) {
        this.headIconOrigWidth = headIconOrigWidth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getIsUserDeskmate() {
        return isUserDeskmate;
    }

    public void setIsUserDeskmate(boolean userDeskmate) {
        isUserDeskmate = userDeskmate;
    }

    public boolean getIsUserHadRole() {
        return isUserHadRole;
    }

    public void setIsUserHadRole(boolean userHadRole) {
        isUserHadRole = userHadRole;
    }

    public int getMaxPutInHouseNum() {
        return maxPutInHouseNum;
    }

    public void setMaxPutInHouseNum(int maxPutInHouseNum) {
        this.maxPutInHouseNum = maxPutInHouseNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public int getUserLikeRoleDefine() {
        return userLikeRoleDefine;
    }

    public void setUserLikeRoleDefine(int userLikeRoleDefine) {
        this.userLikeRoleDefine = userLikeRoleDefine;
    }

    public String getUserLikeRoleDefineTxt() {
        return userLikeRoleDefineTxt;
    }

    public void setUserLikeRoleDefineTxt(String userLikeRoleDefineTxt) {
        this.userLikeRoleDefineTxt = userLikeRoleDefineTxt;

    }

    public String getShowHeadIcon() {
        return showHeadIcon;
    }

    public void setShowHeadIcon(String showHeadIcon) {
        this.showHeadIcon = showHeadIcon;
    }

    @Override
    public String toString() {
        return "RoleInfoEntity{" +
                "headIcon='" + headIcon + '\'' +
                ", headIconMd5='" + headIconMd5 + '\'' +
                ", headIconOrigHeight=" + headIconOrigHeight +
                ", headIconOrigWidth=" + headIconOrigWidth +
                ", id='" + id + '\'' +
                ", isUserDeskmate=" + isUserDeskmate +
                ", getIsUserHadRole=" + isUserHadRole +
                ", maxPutInHouseNum=" + maxPutInHouseNum +
                ", name='" + name + '\'' +
                ", roleType='" + roleType + '\'' +
                ", userLikeRoleDefine=" + userLikeRoleDefine +
                ", userLikeRoleDefineTxt='" + userLikeRoleDefineTxt + '\'' +
                ", showHeadIcon='" + showHeadIcon + '\'' +
                ", getIsPutInHouse=" + isPutInHouse +
                '}';
    }
}
