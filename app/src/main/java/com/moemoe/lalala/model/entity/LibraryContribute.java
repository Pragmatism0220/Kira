package com.moemoe.lalala.model.entity;

/**
 * Created by Hygge on 2018/7/19.
 */

public class LibraryContribute {
    
    private String departmentId;
    private String type;
    private String  targetId;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetId() {

        return targetId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }


    public String getDepartmentId() {
        return departmentId;
    }
}
