package com.moemoe.lalala.model.entity;

/**
 * Created by Administrator on 2018/6/11.
 */

public class NewStoryInfoEventEntity {
    private String bgImage;
    private int branchN;
    private int branchR;
    private int branchSR;
    private int branchTeDian;//特典个数
    private int masterCollectPercent;//主线剧情收集度

    public String getBgImage() {
        return bgImage;
    }

    public void setBgImage(String bgImage) {
        this.bgImage = bgImage;
    }

    public int getBranchN() {
        return branchN;
    }

    public void setBranchN(int branchN) {
        this.branchN = branchN;
    }

    public int getBranchR() {
        return branchR;
    }

    public void setBranchR(int branchR) {
        this.branchR = branchR;
    }

    public int getBranchSR() {
        return branchSR;
    }

    public void setBranchSR(int branchSR) {
        this.branchSR = branchSR;
    }

    public int getBranchTeDian() {
        return branchTeDian;
    }

    public void setBranchTeDian(int branchTeDian) {
        this.branchTeDian = branchTeDian;
    }

    public int getMasterCollectPercent() {
        return masterCollectPercent;
    }

    public void setMasterCollectPercent(int masterCollectPercent) {
        this.masterCollectPercent = masterCollectPercent;
    }
}
