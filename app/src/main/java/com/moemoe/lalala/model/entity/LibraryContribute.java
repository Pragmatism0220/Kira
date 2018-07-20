package com.moemoe.lalala.model.entity;

/**
 * Created by Hygge on 2018/7/19.
 */

public class LibraryContribute {
    
    private String departmentId;
    private String folderId;
    private String type;
    private String  fileId;

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileId() {
        return fileId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getDepartmentId() {
        return departmentId;
    }
}
