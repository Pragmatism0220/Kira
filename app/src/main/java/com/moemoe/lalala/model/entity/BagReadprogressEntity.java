package com.moemoe.lalala.model.entity;

public class BagReadprogressEntity {
    private String targetId;
    private String type;
    private double readProgress;


    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTargetId() {

        return targetId;
    }

    public void setReadProgress(double readProgress) {
        this.readProgress = readProgress;
    }

    public double getReadProgress() {

        return readProgress;
    }

    public String getType() {
        return type;
    }
}
