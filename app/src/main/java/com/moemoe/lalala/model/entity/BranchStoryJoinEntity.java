package com.moemoe.lalala.model.entity;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Hygge on 2018/6/13.
 */

public class BranchStoryJoinEntity {
    private String destBranchStoryId;
    private Map<String,Integer> sourceBranchStoryIds;

    public void setDestBranchStoryId(String destBranchStoryId) {
        this.destBranchStoryId = destBranchStoryId;
    }

    public void setSourceBranchStoryIds(Map<String,Integer> sourceBranchStoryIds) {
        this.sourceBranchStoryIds = sourceBranchStoryIds;
    }

    public String getDestBranchStoryId() {
        return destBranchStoryId;
    }

    public Map<String,Integer> getSourceBranchStoryIds() {
        return sourceBranchStoryIds;
    }
}
