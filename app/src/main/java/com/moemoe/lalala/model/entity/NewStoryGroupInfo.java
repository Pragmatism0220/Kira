package com.moemoe.lalala.model.entity;

/**
 * Created by Administrator on 2018/6/11.
 */

public class NewStoryGroupInfo {

    private String content;//说明
    private String groupName;// 主线剧情组名称
    private String id;// 主线剧情组ID
    private String image;// 封面图
    private boolean isFullCollect;//是否全收集，true：全收集
    private boolean isLock;//是否解锁
    private int joinNum;//参与人数
    private int progress;//进行中的剧情进度

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isFullCollect() {
        return isFullCollect;
    }

    public void setFullCollect(boolean fullCollect) {
        isFullCollect = fullCollect;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public int getJoinNum() {
        return joinNum;
    }

    public void setJoinNum(int joinNum) {
        this.joinNum = joinNum;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
    
}
