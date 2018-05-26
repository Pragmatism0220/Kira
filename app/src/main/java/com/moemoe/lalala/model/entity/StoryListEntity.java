package com.moemoe.lalala.model.entity;

/**
 * Created by Sora on 2018/4/8.
 */

public class StoryListEntity {

    private boolean flag;
    private String storyId;
    private String storyName;

    public boolean isFlag() {
        return flag;
    }

    public String getStoryId() {
        return storyId;
    }

    public String getStoryName() {
        return storyName;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }
}
