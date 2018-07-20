package com.moemoe.lalala.model.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hygge on 2018/6/12.
 */

public class BranchStoryAllEntity implements Serializable, Cloneable {
    private String authorBy;
    private String coverImage;
    private String id;
    private boolean isCommonRole;
    private int level;//1:N、2:R、3:SR、4:限定
    private String name;
    private String painterBy;
    private String roleId;
    private String roleName;
    private String scriptId;
    private int userBranchLevelCount;
    private int holeCount;
    private int holeLevel;//档次，1:N、2:R、3:SR、4:限定） 
    private boolean isShowJoinButton;
    private ArrayList<String> detailImage;
    private String xml;
    private String json;

    private boolean branchShowNews;

    public boolean isBranchShowNews() {
        return branchShowNews;
    }

    public void setBranchShowNews(boolean branchShowNews) {
        this.branchShowNews = branchShowNews;
    }

    public BranchStoryAllEntity clone() {
        BranchStoryAllEntity o = null;
        try {
            o = (BranchStoryAllEntity) super.clone(); //调用object类的clone原生方法对内存块数据字段到字段的赋值
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public void setDetailImage(ArrayList<String> detailImage) {
        this.detailImage = detailImage;
    }

    public ArrayList<String> getDetailImage() {
        return detailImage;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public void setShowJoinButton(boolean showJoinButton) {
        isShowJoinButton = showJoinButton;
    }

    public boolean isShowJoinButton() {
        return isShowJoinButton;
    }

    public int getHoleCount() {
        return holeCount;
    }

    public int getHoleLevel() {
        return holeLevel;
    }

    public void setHoleCount(int holeCount) {
        this.holeCount = holeCount;
    }

    public void setHoleLevel(int holeLevel) {
        this.holeLevel = holeLevel;
    }

    public void setAuthorBy(String authorBy) {
        this.authorBy = authorBy;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCommonRole(boolean commonRole) {
        isCommonRole = commonRole;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPainterBy(String painterBy) {
        this.painterBy = painterBy;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setScriptId(String scriptId) {
        this.scriptId = scriptId;
    }

    public void setUserBranchLevelCount(int userBranchLevelCount) {
        this.userBranchLevelCount = userBranchLevelCount;
    }

    public String getAuthorBy() {
        return authorBy;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public String getId() {
        return id;
    }

    public boolean isCommonRole() {
        return isCommonRole;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public String getPainterBy() {
        return painterBy;
    }

    public String getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getScriptId() {
        return scriptId;
    }

    public int getUserBranchLevelCount() {
        return userBranchLevelCount;
    }
}
