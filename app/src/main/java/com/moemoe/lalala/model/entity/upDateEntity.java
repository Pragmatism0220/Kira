package com.moemoe.lalala.model.entity;

/**
 * Created by Administrator on 2018/7/17.
 */

public class upDateEntity {
    private String funName;
    private String targetId;

    public upDateEntity(String funName, String targetId) {
        this.funName = funName;
        this.targetId = targetId;
    }

    public String getFunName() {
        return funName;
    }

    public void setFunName(String funName) {
        this.funName = funName;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
