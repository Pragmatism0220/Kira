package com.moemoe.lalala.model.entity;

/**
 * Created by Administrator on 2018/7/17.
 */

public class SearchNewListEntity {

    private String funName;//
    private String targetId;// 关联对象ID

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
