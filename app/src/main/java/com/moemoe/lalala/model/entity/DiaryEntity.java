package com.moemoe.lalala.model.entity;

import java.util.ArrayList;

/**
 * Created by zhangyan on 2018/6/13.
 */

public class DiaryEntity {

    private ArrayList<DiaryDetailsEntity> diaryDetails;//日记明细（多条
    private int hadRoleClothesCount;//拥有服装件数 ,
    private int hadRoleDays;//角色拥有的天数
    private int hadRoleStoryCount;//拥有该角色的支线剧情个数
    private String id;//羁绊日记角色ID
    private String roleClothesSum;//服装全部件数
    private int roleLike;//好感度当前值
    private String roleLikeLevelColor;//好感度等级颜色
    private String roleLikeLevelName;//好感度等级描述
    private int roleLikeMax;//好感度当前档次最大值
    private String roleName;//角色名称
    private String roleStorySum;// 该角色一共有多个剧情
    private String roleId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public ArrayList<DiaryDetailsEntity> getDiaryDetails() {
        return diaryDetails;
    }

    public void setDiaryDetails(ArrayList<DiaryDetailsEntity> diaryDetails) {
        this.diaryDetails = diaryDetails;
    }

    public int getHadRoleClothesCount() {
        return hadRoleClothesCount;
    }

    public void setHadRoleClothesCount(int hadRoleClothesCount) {
        this.hadRoleClothesCount = hadRoleClothesCount;
    }

    public int getHadRoleDays() {
        return hadRoleDays;
    }

    public void setHadRoleDays(int hadRoleDays) {
        this.hadRoleDays = hadRoleDays;
    }

    public int getHadRoleStoryCount() {
        return hadRoleStoryCount;
    }

    public void setHadRoleStoryCount(int hadRoleStoryCount) {
        this.hadRoleStoryCount = hadRoleStoryCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleClothesSum() {
        return roleClothesSum;
    }

    public void setRoleClothesSum(String roleClothesSum) {
        this.roleClothesSum = roleClothesSum;
    }

    public int getRoleLike() {
        return roleLike;
    }

    public void setRoleLike(int roleLike) {
        this.roleLike = roleLike;
    }

    public String getRoleLikeLevelColor() {
        return roleLikeLevelColor;
    }

    public void setRoleLikeLevelColor(String roleLikeLevelColor) {
        this.roleLikeLevelColor = roleLikeLevelColor;
    }

    public String getRoleLikeLevelName() {
        return roleLikeLevelName;
    }

    public void setRoleLikeLevelName(String roleLikeLevelName) {
        this.roleLikeLevelName = roleLikeLevelName;
    }

    public int getRoleLikeMax() {
        return roleLikeMax;
    }

    public void setRoleLikeMax(int roleLikeMax) {
        this.roleLikeMax = roleLikeMax;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleStorySum() {
        return roleStorySum;
    }

    public void setRoleStorySum(String roleStorySum) {
        this.roleStorySum = roleStorySum;
    }
}
