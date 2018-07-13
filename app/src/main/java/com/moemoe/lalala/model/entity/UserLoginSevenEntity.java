package com.moemoe.lalala.model.entity;

/**
 * Created by Administrator on 2018/7/13.
 */

public class UserLoginSevenEntity {

    private int days;//当前已完成任务的天数 ,
    private boolean docStatus;//社团发帖
    private boolean loginStatus;//登录Kira学院  true
    private boolean replyDocStatus;// 回复帖子
    private boolean rewardStatus;// 领取今日奖励
    private boolean seeDepartmentStatus;// 浏览学部

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public boolean isDocStatus() {
        return docStatus;
    }

    public void setDocStatus(boolean docStatus) {
        this.docStatus = docStatus;
    }

    public boolean isLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    public boolean isReplyDocStatus() {
        return replyDocStatus;
    }

    public void setReplyDocStatus(boolean replyDocStatus) {
        this.replyDocStatus = replyDocStatus;
    }

    public boolean isRewardStatus() {
        return rewardStatus;
    }

    public void setRewardStatus(boolean rewardStatus) {
        this.rewardStatus = rewardStatus;
    }

    public boolean isSeeDepartmentStatus() {
        return seeDepartmentStatus;
    }

    public void setSeeDepartmentStatus(boolean seeDepartmentStatus) {
        this.seeDepartmentStatus = seeDepartmentStatus;
    }
}
