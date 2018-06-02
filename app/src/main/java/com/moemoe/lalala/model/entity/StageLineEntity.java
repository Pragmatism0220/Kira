package com.moemoe.lalala.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hygge on 2018/6/2.
 */

public class StageLineEntity {

    private String id;
    private String roleId;
    private String roleName;
    private String content;
    private String schema;
    private String dialogType;
    private List<StageLineOptionsEntity> options;
    private String parentOptionId;

    private List<StageLineEntity> children = new ArrayList<StageLineEntity>();

    public String getId() {
        return id;
    }

    public String getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getContent() {
        return content;
    }

    public String getSchema() {
        return schema;
    }

    public String getDialogType() {
        return dialogType;
    }

    public List<StageLineOptionsEntity> getOptions() {
        return options;
    }

    public String getParentOptionId() {
        return parentOptionId;
    }

    public List<StageLineEntity> getChildren() {
        return children;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setDialogType(String dialogType) {
        this.dialogType = dialogType;
    }

    public void setOptions(List<StageLineOptionsEntity> options) {
        this.options = options;
    }

    public void setParentOptionId(String parentOptionId) {
        this.parentOptionId = parentOptionId;
    }

    public void setChildren(List<StageLineEntity> children) {
        this.children = children;
    }
}
