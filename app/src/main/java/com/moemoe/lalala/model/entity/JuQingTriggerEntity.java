package com.moemoe.lalala.model.entity;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonObject;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

import java.util.ArrayList;

/**
 * Created by yi on 2017/9/27.
 */
@Entity
public class JuQingTriggerEntity {


    @Id
    private String id;
    private String extra;
    private boolean force;//是否为强制剧情
    private int level;//1:主线 2:支线 3:日常 ,
    private String roleOf;//关联的角色
    private String storyId;//关联剧情ID
    private String type;//map:地图剧情,mobile:手机剧情
    private String conditionStr;
    private String groupId;//主线剧情分组ID
    private String scriptId;//主线剧情ID
    private String fileName;
    private int downloadState;//1.未下载 2.下载完成 3.下载失败

    private String iconId;
    private String iconPath;
    private int w;
    private int h;
    private int x;
    private int y;
    private String md5;
    @Transient
    private JsonArray condition;


    public JuQingTriggerEntity(NewJuQingTriggerEntity entity, String type) {
        id = entity.getId();


        condition = entity.getCondition();
        conditionStr = entity.getCondition().toString();
        scriptId = entity.getScriptId();
        force = entity.isForce();
        level = entity.getLevel();
        roleOf = entity.getRoleOf();
        storyId = entity.getStoryId();
        groupId = entity.getGroupId();

        extra = entity.getExtra();
        JsonObject jsonObject = new Gson().fromJson(extra, JsonObject.class);

        String iconId = jsonObject.get("iconId").getAsString();
        this.iconId = iconId;
        String iconPath = jsonObject.get("icon").getAsString();
        this.iconPath = iconPath;

        int w = jsonObject.get("w").getAsInt();
        this.w = w;
        int h = jsonObject.get("h").getAsInt();
        this.h = h;
        int x = jsonObject.get("x").getAsInt();
        this.x = x;
        int y = jsonObject.get("y").getAsInt();
        this.y = y;
        String md5 = jsonObject.get("md5").getAsString();
        this.md5 = md5;


        fileName = entity.getId() + iconPath.substring(iconPath.lastIndexOf("."));
        downloadState = 1;
        this.type = type;
    }




    @Generated(hash = 1163253950)
    public JuQingTriggerEntity(String id, String extra, boolean force, int level, String roleOf, String storyId,
            String type, String conditionStr, String groupId, String scriptId, String fileName, int downloadState,
            String iconId, String iconPath, int w, int h, int x, int y, String md5) {
        this.id = id;
        this.extra = extra;
        this.force = force;
        this.level = level;
        this.roleOf = roleOf;
        this.storyId = storyId;
        this.type = type;
        this.conditionStr = conditionStr;
        this.groupId = groupId;
        this.scriptId = scriptId;
        this.fileName = fileName;
        this.downloadState = downloadState;
        this.iconId = iconId;
        this.iconPath = iconPath;
        this.w = w;
        this.h = h;
        this.x = x;
        this.y = y;
        this.md5 = md5;
    }




    @Generated(hash = 901926953)
    public JuQingTriggerEntity() {
    }




    public static ArrayList<JuQingTriggerEntity> toDb(ArrayList<NewJuQingTriggerEntity> entities, String type) {
        ArrayList<JuQingTriggerEntity> res = new ArrayList<>();
        for (NewJuQingTriggerEntity entity : entities) {
            JuQingTriggerEntity entity1 = new JuQingTriggerEntity(entity, type);
            res.add(entity1);
        }
        return res;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JsonArray getCondition() {
        return condition;
    }

    public void setCondition(JsonArray condition) {
        this.condition = condition;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getRoleOf() {
        return roleOf;
    }

    public void setRoleOf(String roleOf) {
        this.roleOf = roleOf;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConditionStr() {
        return conditionStr;
    }

    public void setConditionStr(String conditionStr) {
        this.conditionStr = conditionStr;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getScriptId() {
        return scriptId;
    }

    public void setScriptId(String scriptId) {
        this.scriptId = scriptId;
    }

    @Override
    public String toString() {
        return "JuQingTriggerEntity{" +
                "id='" + id + '\'' +
                ", extra='" + extra + '\'' +
                ", force=" + force +
                ", level=" + level +
                ", roleOf='" + roleOf + '\'' +
                ", storyId='" + storyId + '\'' +
                ", type='" + type + '\'' +
                ", conditionStr='" + conditionStr + '\'' +
                ", groupId='" + groupId + '\'' +
                ", scriptId='" + scriptId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", downloadState=" + downloadState +
                ", iconId='" + iconId + '\'' +
                ", iconPath='" + iconPath + '\'' +
                ", w=" + w +
                ", h=" + h +
                ", x=" + x +
                ", y=" + y +
                ", md5='" + md5 + '\'' +
                ", condition=" + condition +
                '}';
    }

    public boolean getForce() {
        return this.force;
    }
}
