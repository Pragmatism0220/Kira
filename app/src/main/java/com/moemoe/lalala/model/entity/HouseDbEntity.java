package com.moemoe.lalala.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;

/**
 * 地图元素数据库
 * Created by yi on 2017/10/10.
 */
@Entity
public class HouseDbEntity {

    @Id
    private String id; // id
    private String name;
    private String image_path;// 图片信息
    private int image_w;
    private int image_h;
    private String schema;// 跳转地址
    private int pointX;// x坐标
    private int pointY;// y坐标
    private String text;// 显示文字
    private String shows;// 时间段显示
    private String fileName;
    private int downloadState;//1.未下载 2.下载完成 3.下载失败
    private String md5;
    private String type;//map allUser birthdayUser followUser nearUser
    private int layer;//图片图层
    private String house;
    private String timerId;//计时器ID
    private boolean timerIsCollectFull;//是否好感度满额，true：好感度满额
    private boolean timerIsCollectable;// 是否可以采集，true：可以采集
    private boolean timerIsSleep;//角色是否在睡觉中，true：睡觉中 
    private String timerLastCollectTime;//上一次采集时间
    private long timerRemainTime;//剩余时间
    private String timerRoleId;//角色ID;
    private int timerRoleLike;//好感动度值

    public HouseDbEntity(MapEntity entity, String type) {
        id = entity.getId();
        image_path = entity.getImage().getPath();
        image_w = entity.getImage().getW();
        image_h = entity.getImage().getH();
        fileName = entity.getId() + image_path.substring(image_path.lastIndexOf("."));
        if (entity.getRoleTimer() != null) {
            this.timerId = entity.getRoleTimer().getId();
            timerIsCollectFull = entity.getRoleTimer().isCollectFull();
            timerIsCollectable = entity.getRoleTimer().isCollectable();
            timerIsSleep = entity.getRoleTimer().isSleep();
            timerLastCollectTime = entity.getRoleTimer().getLastCollectTime();
            timerRemainTime = entity.getRoleTimer().getRemainTime();
            timerRoleId = entity.getRoleTimer().getRoleId();
            timerRoleLike = entity.getRoleTimer().getRoleLike();
        }
        schema = entity.getSchema();
        pointX = entity.getPointX();
        pointY = entity.getPointY();
        text = entity.getText();
        shows = entity.getShows();
        name = entity.getName();
        md5 = entity.getMd5();
        downloadState = 1;
        this.type = "" + entity.getType();
        house = type;
        layer = entity.getLayer();
    }

    @Generated(hash = 1831665315)
    public HouseDbEntity(String id, String name, String image_path, int image_w, int image_h,
                         String schema, int pointX, int pointY, String text, String shows, String fileName,
                         int downloadState, String md5, String type, int layer, String house,
                         String timerId, boolean timerIsCollectFull, boolean timerIsCollectable,
                         boolean timerIsSleep, String timerLastCollectTime, long timerRemainTime,
                         String timerRoleId, int timerRoleLike) {
        this.id = id;
        this.name = name;
        this.image_path = image_path;
        this.image_w = image_w;
        this.image_h = image_h;
        this.schema = schema;
        this.pointX = pointX;
        this.pointY = pointY;
        this.text = text;
        this.shows = shows;
        this.fileName = fileName;
        this.downloadState = downloadState;
        this.md5 = md5;
        this.type = type;
        this.layer = layer;
        this.house = house;
        this.timerId = timerId;
        this.timerIsCollectFull = timerIsCollectFull;
        this.timerIsCollectable = timerIsCollectable;
        this.timerIsSleep = timerIsSleep;
        this.timerLastCollectTime = timerLastCollectTime;
        this.timerRemainTime = timerRemainTime;
        this.timerRoleId = timerRoleId;
        this.timerRoleLike = timerRoleLike;
    }

    @Generated(hash = 1369071674)
    public HouseDbEntity() {
    }

    public static ArrayList<HouseDbEntity> toDb(ArrayList<MapEntity> entities, String type) {
        ArrayList<HouseDbEntity> res = new ArrayList<>();
        for (MapEntity entity : entities) {
            HouseDbEntity entity1 = new HouseDbEntity(entity, type);
            res.add(entity1);
        }
        return res;
    }

    public void setTimerId(String timerId) {
        this.timerId = timerId;
    }

    public void setTimerIsCollectFull(boolean timerIsCollectFull) {
        this.timerIsCollectFull = timerIsCollectFull;
    }

    public void setTimerIsCollectable(boolean timerIsCollectable) {
        this.timerIsCollectable = timerIsCollectable;
    }

    public void setTimerIsSleep(boolean timerIsSleep) {
        this.timerIsSleep = timerIsSleep;
    }

    public void setTimerLastCollectTime(String timerLastCollectTime) {
        this.timerLastCollectTime = timerLastCollectTime;
    }

    public void setTimerRemainTime(long timerRemainTime) {
        this.timerRemainTime = timerRemainTime;
    }

    public void setTimerRoleId(String timerRoleId) {
        this.timerRoleId = timerRoleId;
    }

    public void setTimerRoleLike(int timerRoleLike) {
        this.timerRoleLike = timerRoleLike;
    }

    public String getTimerId() {
        return timerId;
    }

    public boolean isTimerIsCollectFull() {
        return timerIsCollectFull;
    }

    public boolean isTimerIsCollectable() {
        return timerIsCollectable;
    }

    public boolean isTimerIsSleep() {
        return timerIsSleep;
    }

    public String getTimerLastCollectTime() {
        return timerLastCollectTime;
    }

    public long getTimerRemainTime() {
        return timerRemainTime;
    }

    public String getTimerRoleId() {
        return timerRoleId;
    }

    public int getTimerRoleLike() {
        return timerRoleLike;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getHouse() {
        return house;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public int getImage_w() {
        return image_w;
    }

    public void setImage_w(int image_w) {
        this.image_w = image_w;
    }

    public int getImage_h() {
        return image_h;
    }

    public void setImage_h(int image_h) {
        this.image_h = image_h;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public int getPointX() {
        return pointX;
    }

    public void setPointX(int pointX) {
        this.pointX = pointX;
    }

    public int getPointY() {
        return pointY;
    }

    public void setPointY(int pointY) {
        this.pointY = pointY;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getShows() {
        return shows;
    }

    public void setShows(String shows) {
        this.shows = shows;
    }

    public int getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public int getLayer() {
        return this.layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public boolean getTimerIsCollectFull() {
        return this.timerIsCollectFull;
    }

    public boolean getTimerIsCollectable() {
        return this.timerIsCollectable;
    }

    public boolean getTimerIsSleep() {
        return this.timerIsSleep;
    }
}
