package com.moemoe.lalala.model.entity;

/**
 * Created by Hygge on 2018/6/25.
 */

public class RubblishBody {
    private String hostUserId;
    private String rubbishId;
    private String toolId;

    public RubblishBody(String hostUserId, String rubbishId,String toolId) {
        this.hostUserId = hostUserId;
        this.rubbishId = rubbishId;
        this.toolId=toolId;
    }

    public String getToolId() {
        return toolId;
    }

    public void setToolId(String toolId) {
        this.toolId = toolId;
    }

    public String getHostUserId() {
        return hostUserId;
    }

    public String getRubbishId() {
        return rubbishId;
    }

    public void setHostUserId(String hostUserId) {
        this.hostUserId = hostUserId;
    }

    public void setRubbishId(String rubbishId) {
        this.rubbishId = rubbishId;
    }
}
