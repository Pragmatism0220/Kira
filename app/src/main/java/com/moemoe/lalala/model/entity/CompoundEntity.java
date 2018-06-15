package com.moemoe.lalala.model.entity;

/**
 * Created by Hygge on 2018/6/12.
 */

public class CompoundEntity {
    private String coverImage;
    private String roleId;
    private int userBranchLevelCount;

    public void setUserBranchLevelCount(int userBranchLevelCount) {
        this.userBranchLevelCount = userBranchLevelCount;
    }

    public int getUserBranchLevelCount() {
        return userBranchLevelCount;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
